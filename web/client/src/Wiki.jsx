import React, { useState, useEffect, useContext, createContext } from 'react';
import { useParams, Link } from 'react-router-dom';
import Markdown from 'react-markdown'
import { Popup } from './App';

function copyText(text) {
    var textArea = document.createElement('textarea');
    textArea.value = text;
    textArea.style.position = 'fixed';
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
        const r = document.execCommand('copy');
        document.body.removeChild(textArea);
        return r;
    } catch (err) {
        document.body.removeChild(textArea);
        return false;
    }

}

const Copy = ({ display, text }) => {
    const [copied, setCopied] = useState(false);
    const [hovered, hover] = useState(false);

    return <span
        className='copy'
        onClick={() => setCopied(copyText(text))}
        onMouseLeave={() => {
            hover(false);
            setCopied(false);
        }}
        onMouseMove={e => hover({ x: e.clientX, y: e.clientY })}
    >
        {display}
        <Popup {...{ hovered }}>
            <p className={copied ? 'copied' : ''}>{!copied ? 'Click to copy:' : 'Copied!'}</p>
            <p>{text}</p>
        </Popup>

    </span>
}

function usePage() {
    const { page: param } = useParams();
    const [page, setPage] = useState();

    const key = param || 'home';

    useEffect(() => {
        fetch(`./wiki/${key}`)
            .then(r => r.text())
            .catch(e => `*${e}*`)
            .then(p => setPage(p))
    }, [key]);

    return page;
}

const MarkdownContainer = ({ children }) => {
    const { page } = useParams();

    const panels = [];
    let nextPanel = [];
    const pushNext = () => {
        if (nextPanel.length > 0) panels.push(nextPanel);
        nextPanel = [];
    }

    const isHeading = e => e.key.includes('heading');

    children.forEach(child => {
        if (child.type === 'hr') pushNext();
        else if (isHeading(child)) {
            pushNext()
            panels.push([child]);
        }
        else nextPanel.push(child);
    });
    pushNext();

    return (
        <div className='markdown'>
            {panels.map((p, i) => <Row key={i}>{p}</Row>)}
        </div>
    )
}

const Row = ({ children }) => {
    const cols = [];
    let next = [];

    const isImage = c => c.type === Image || (c.props && c.props.children && isImage(c.props.children[0]));

    const pushNext = () => {
        if (next.length > 0) cols.push(<p key={cols.length}>{next}</p>);
        next = [];
    }
    children.forEach(child => {
        if (isImage(child)) {
            pushNext();
            cols.push(child);
        } else next.push(child);
    })
    pushNext();

    return (
        <div className='row'>
            {cols}
        </div>
    )
}

const MarkdownLink = ({ href, alt, children }) => <Link to={href} {...{ alt }}>{children}</Link>

const getImage = uri => {
    try {
        return require(`./img/wiki/${uri}`);
    } catch (_) {
        return 'https://praxis-siebers.de/wp-content/uploads/2016/10/orionthemes-placeholder-image.png';
    }
}

const MarkdownCopy = ({ children }) => {
    if (typeof children === 'string') {
        if (children.includes(':')) {
            const [d, ...t] = children.split(':')
            return <Copy text={t.join(':')} display={d.trim()} />
        }
        return <Copy text={children} display={children} />
    }
    return null;
}


const Lightroom = () => {
    const [props, view] = useContext(LightroomContext);

    const close = e => {
        if (e.target.classList.contains('lightroom')) view();
    }

    return (props ?
        <div className='lightroom' onClick={close}>
            < img {...props} />
        </div > : null
    );

}

const Image = (props) => {
    const [, view] = useContext(LightroomContext);

    return (
        <img
            onClick={() => view(props)}
            {...props}
        />
    )
}

const LightroomContext = createContext();
function Wiki() {
    const page = usePage();
    const lightroom = useState(null);

    return (
        <LightroomContext.Provider value={lightroom}>
            <Lightroom />
            {page ?
                <Markdown
                    source={page}
                    renderers={{
                        root: MarkdownContainer,
                        link: MarkdownLink,
                        inlineCode: MarkdownCopy,
                        paragraph: ({ children }) => <>{children}</>,
                        image: Image,
                        table: p => <div className='table'>{p.children}</div>,
                        tableHead: p => <>{p.children}</>,
                        tableBody: p => <>{p.children}</>,
                        tableRow: p => <div className='row'>{p.children}</div>,
                        tableCell: p => <div>{p.children}</div>,
                    }}
                    transformImageUri={getImage}
                />
                : <p>Loading...</p>}
        </LightroomContext.Provider>
    )
}

export default Wiki;