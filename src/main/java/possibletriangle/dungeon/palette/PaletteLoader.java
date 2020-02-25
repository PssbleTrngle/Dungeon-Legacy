package possibletriangle.dungeon.palette;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.placeholder.Type;
import possibletriangle.dungeon.helper.Pair;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PaletteLoader extends ReloadListener<List<Supplier<Palette>>> {

    private final NetworkTagManager tags;
    public PaletteLoader(NetworkTagManager tags) {
        this.tags = tags;
    }

    @Override
    protected List<Supplier<Palette>> prepare(IResourceManager manager, IProfiler profiler) {

        Collection<ResourceLocation> resources = manager.getAllResourceLocations("palettes", s -> s.endsWith(".xml"));
        DungeonMod.LOGGER.info("Found {} palettes", resources.size());

        return resources.stream()
                .map(r -> load(manager, r))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    protected void apply(List<Supplier<Palette>> list, IResourceManager manager, IProfiler profiler) {
        DungeonMod.LOGGER.info("Loaded {} palettes", list.size());
        Palette.clear();
        list.stream().map(Supplier::get).forEach(Palette::register);
    }

    private Collection<Supplier<Palette>> load(IResourceManager manager, ResourceLocation path) {
        try {
            String p = path.getPath();
            ResourceLocation name = new ResourceLocation(path.getNamespace(), p.substring(p.lastIndexOf('/') + 1, p.length() - 4));
            return manager.getAllResources(path).stream()
                    .map(r -> parse(r.getInputStream(), name))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            return Lists.newArrayList();
        }
    }

    private static Schema getSchema() throws SAXException {
        File file = new File("C:\\Users\\firef\\Coding\\Java\\MC\\1.14\\Dungeon\\src\\main\\resources\\data\\dungeon\\palettes\\palette.xsd");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return factory.newSchema(file);
    }

    private static Stream<Element> elements(Element parent, String name) {
        NodeList list = parent.getChildNodes();
        return IntStream.range(0, list.getLength())
                .mapToObj(list::item)
                .filter(e -> e.getNodeName().equalsIgnoreCase(name))
                .filter(Element.class::isInstance)
                .map(n -> (Element) n);
    }

    private static Optional<Block> findBlock(ResourceLocation name) {
        if(!GameRegistry.findRegistry(Block.class).containsKey(name)) {
            DungeonMod.LOGGER.warn("Could not find block '{}'", name);
            return Optional.empty();
        }
        return Optional.ofNullable(GameRegistry.findRegistry(Block.class).getValue(name));
    }

    public static class StateProviderSupplier {
        private float weight;
        public final Supplier<Optional<Stream<IStateProvider>>> supplier;

        public StateProviderSupplier(Supplier<Optional<Stream<IStateProvider>>> supplier) {
            this.supplier = supplier;
        }

        float getWeight() {
            return weight;
        }

        StateProviderSupplier setWeight(float weight) {
            this.weight = weight;
            return this;
        }

        public <T> Optional<Stream<T>> map(Function<IStateProvider,T> consumer) {
            return this.supplier.get().map(s -> s.map(consumer));
        }

        public void forEach(Consumer<IStateProvider> consumer) {
            this.supplier.get().ifPresent(s -> s.forEach(consumer));
        }
    }

    private Optional<Tag<Block>> findTag(ResourceLocation name) {
        boolean isMC = name.getNamespace().equals("minecraft");
        return Optional.ofNullable(tags.getBlocks().get(name)).map(Optional::of).orElseGet(
                isMC ? () -> findTag(new ResourceLocation("forge", name.getPath())) : () -> {
                    DungeonMod.LOGGER.warn("Could not find tag '{}'", name);
                    return Optional.empty();
                });
    }

    private static PropertyProvider[] findProperties(Element e) {
        return Stream.of(

                elements(e, "set").map(c -> {
                    String value = c.getAttribute("value");
                    return new PropertyProvider(e.getAttribute("key"), (r, p) -> p.parseValue(value).map(Function.identity()));
                }),

                elements(e, "cycle").map(c -> new PropertyProvider(e.getAttribute("key"), (r, p) -> {
                    Comparable[] v = p.getAllowedValues().toArray(new Comparable[0]);
                    return Optional.of(v[r.nextInt(v.length)]);
                }))

        ).flatMap(Function.identity()).toArray(PropertyProvider[]::new);
    }

    private Optional<StateProviderSupplier> getProvider(Element e, String type) {
        String id = e.getAttribute("id");
        if(id.startsWith("#")) id = id.substring(1);
        String mod = e.getAttribute("mod");
        ResourceLocation r = new ResourceLocation(mod, id);
        Stream<StateProviderSupplier> children = findProviders(e);

        switch (type) {
            case "tag":
                return Optional.of(new StateProviderSupplier(() -> findTag(r)
                        .map(Tag::getAllElements)
                        .map(Collection::stream)
                        .map(s -> s.map(BlockProvider::new))
                ));

            case "block":
                return Optional.of(new StateProviderSupplier(() ->
                        findBlock(r).map(BlockProvider::new).map(Stream::of)
                ));

            case"collection":
                return Optional.of(new StateProviderSupplier(() -> {
                    BlockCollection collection = new BlockCollection();
                    children.forEach(provider -> provider.forEach(p -> collection.add(p, provider.weight)));
                    return Optional.of(collection).map(Stream::of);
                }));

            case "variant":
                return Optional.of(new StateProviderSupplier(() -> Optional.of(new Variant(children.map(provider -> provider.map(p -> p))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .flatMap(Function.identity())
                        .toArray(IStateProvider[]::new)
                )).map(Stream::of)));

            case "fallback":
                return Optional.of(new StateProviderSupplier(() -> Optional.of(new Fallback(children.map(provider -> provider.map(p -> p))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .flatMap(Function.identity())
                        .toArray(IStateProvider[]::new)
                )).map(Stream::of)));

            default: return Optional.empty();

        }
    }

    private Stream<StateProviderSupplier> findProviders(Element parent) {
        NodeList list = parent.getChildNodes();
        return IntStream.range(0, list.getLength())
                .mapToObj(list::item)
                .filter(Element.class::isInstance)
                .map(e -> (Element) e)
                .map(e -> getProvider( e, e.getNodeName().toLowerCase()).map(p -> p.setWeight(getWeight(e))))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(p -> {
                    PropertyProvider[] properties = findProperties(e);
                    if(p instanceof StateProvider) ((StateProvider) p).setProperties(properties);
                });
    }

    private static float getWeight(Element e) {
        try {
            return Float.parseFloat(e.getAttribute("float"));
        } catch (NumberFormatException ex) {
            return 1F;
        }
    }

    private Optional<Supplier<Palette>> parse(InputStream input, ResourceLocation name) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(getSchema());
            //factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
            //        "http://www.w3.org/2001/XMLSchema");
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler());

            Document xml = builder.parse(input);

            Element paletteNode = xml.getDocumentElement();
            paletteNode.normalize();

            float weight = Float.parseFloat(paletteNode.getAttribute("weight"));
            Biome biome = GameRegistry.findRegistry(Biome.class).getValue(new ResourceLocation(paletteNode.getAttribute("biome")));
            final String parent = paletteNode.hasAttribute("parent") ? paletteNode.getAttribute("parent") : "dungeon:stone";

            List<Pair<Type[],List<StateProviderSupplier>>> replaces = elements(paletteNode, "replace").map(replace -> {
                Type[] types = elements(replace, "type")
                        .map(Node::getTextContent)
                        .map(Type::byName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toArray(Type[]::new);

                return new Pair<>(types, findProviders(replace).collect(Collectors.toList()));
            }).collect(Collectors.toList());

            return Optional.of(() -> {
                Palette palette = new Palette(name, weight, () -> Optional.ofNullable(biome).orElse(Biomes.THE_VOID), new ResourceLocation(parent));

                replaces.forEach(pair -> {
                    Type[] types = pair.getFirst();
                     IStateProvider[] providers = pair.getSecond().stream()
                            .map(s -> s.supplier.get())
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .flatMap(Function.identity())
                            .toArray(IStateProvider[]::new);

                     if (providers.length == 1) palette.put(providers[0], types);
                     else if(providers.length > 0) palette.put(new BlockCollection(providers), types);
                });

                return palette;
            });

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            return Optional.empty();
        }
    }

    public static class ErrorHandler implements org.xml.sax.ErrorHandler {

        @Override
        public void warning(SAXParseException e) {
            DungeonMod.LOGGER.warn("Palette XML Warning: {}", e.getMessage());
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            DungeonMod.LOGGER.error("Palette XML Error: {}", e.getMessage());
            throw e;
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            DungeonMod.LOGGER.error("Palette XML Fatal Error: {}", e.getMessage());
            throw e;
        }
    }

}
