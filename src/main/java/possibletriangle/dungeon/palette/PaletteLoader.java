package possibletriangle.dungeon.palette;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.placeholder.Type;
import possibletriangle.dungeon.helper.Pair;

import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PaletteLoader extends ReloadListener<List<Palette>> {

    @Override
    protected List<Palette> prepare(IResourceManager manager, IProfiler profiler) {

        Collection<ResourceLocation> resources = manager.getAllResourceLocations("palettes", s -> s.endsWith(".xml"));
        DungeonMod.LOGGER.info("Found {} palettes", resources.size());

        return resources.stream()
                .map(r -> load(manager, r))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    protected void apply(List<Palette> list, IResourceManager manager, IProfiler profiler) {
        DungeonMod.LOGGER.info("Loaded {} palettes", list.size());
        Palette.clear();
        list.forEach(Palette::register);
    }

    private static Collection<Palette> load(IResourceManager manager, ResourceLocation path) {
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

    private static InputStream getSchema() throws FileNotFoundException {
        return new FileInputStream(new File("C:\\Users\\firef\\Coding\\Java\\MC\\1.14\\Dungeon\\src\\main\\resources\\data\\dungeon\\palettes\\palette.xsd"));
    }

    private static Stream<Element> elements(Element parent, String name) {
        NodeList list = parent.getChildNodes();
        return IntStream.range(0, list.getLength())
                .mapToObj(list::item)
                .filter(e -> e.getNodeName().equalsIgnoreCase(name))
                .map(n -> (Element) n);
    }

    private static Optional<Block> findBlock(ResourceLocation name) {
        if(!GameRegistry.findRegistry(Block.class).containsKey(name)) {
            DungeonMod.LOGGER.warn("Could not find block '{}'", name);
            return Optional.empty();
        }
        return Optional.ofNullable(GameRegistry.findRegistry(Block.class).getValue(name));
    }

    private static <V extends Comparable<V>> Optional<IProperty<V>> findProperty(Block block, Element e) {
        String key = e.getAttribute("key");
        return block.getDefaultState().getProperties().stream()
                .filter(p -> p.getName().equalsIgnoreCase(key))
                .findFirst()
                .map(p -> (IProperty<V>) p);
    }

    private static <V extends Comparable<V>> Function<Random,V> cycle(IProperty<V> p) {
        return r -> {
            Object[] v = p.getAllowedValues().toArray();
            return (V) v[r.nextInt(v.length)];
        };
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

    private static Stream<Pair<? extends IStateProvider,Float>> findProviders(Element parent) {
        return Stream.of(

            elements(parent, "block").map(e -> {
                String id = e.getAttribute("id");
                String mod = e.getAttribute("mod");
                ResourceLocation r = new ResourceLocation(mod, id);
                return findBlock(r).map(b -> new Pair<>(new BlockProvider(b), e));
            })
            .filter(Optional::isPresent)
            .map(Optional::get),

            elements(parent, "collection").map(e -> {
                    BlockCollection collection = new BlockCollection();
                    findProviders(e).forEach(pair -> collection.add(pair.getFirst(), pair.getSecond()));
                    return new Pair<>(collection, e);
                }),

            elements(parent, "variant").map(e -> {
                Variant variant = new Variant(findProviders(e)
                    .map(Pair::getFirst)
                    .toArray(IStateProvider[]::new)
                );
                return new Pair<>(variant, e);
            }),

            elements(parent, "fallback").map(e -> {
                Fallback fallback = new Fallback(findProviders(e)
                        .map(Pair::getFirst)
                        .toArray(IStateProvider[]::new)
                );
                return new Pair<>(fallback, e);
            })

        ).flatMap(Function.identity()).map(p -> {
            Element e = p.getSecond();
            return new Pair<>(p.getFirst().setProperties(findProperties(e)), getWeight(e));
        });
    }

    private static float getWeight(Element e) {
        try {
            return Float.parseFloat(e.getAttribute("float"));
        } catch (NumberFormatException ex) {
            return 1F;
        }
    }

    private static Optional<Palette> parse(InputStream input, ResourceLocation name) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler());

            Document xml = builder.parse(input);
            Element paletteNode = xml.getDocumentElement();
            paletteNode.normalize();

            float weight = Float.parseFloat(paletteNode.getAttribute("weight"));
            Biome biome = GameRegistry.findRegistry(Biome.class).getValue(new ResourceLocation(paletteNode.getAttribute("biome")));
            String parent = paletteNode.getAttribute("parent");

            Palette palette = new Palette(name, weight, () -> Optional.ofNullable(biome).orElse(Biomes.THE_VOID), new ResourceLocation(parent));

            elements(paletteNode, "replace").forEach(replace -> {
                Type[] types = elements(replace, "type")
                        .map(Node::getTextContent)
                        .map(Type::byName)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toArray(Type[]::new);
                findProviders(replace).map(Pair::getFirst).findFirst().ifPresent(block -> palette.put(block, types));
            });

            return Optional.of(palette);

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
