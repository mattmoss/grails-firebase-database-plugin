import React from 'react';

const ChannelListView = ({ channels }) => (
    <ul>
        {channels.map(({ key, name, topic }) =>
            <li key={key}>{name} - {topic}</li>
        )}
    </ul>
);

export default ChannelListView;
