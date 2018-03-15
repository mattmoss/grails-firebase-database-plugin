import React from 'react';
import { ListGroup, ListGroupItem } from 'react-bootstrap';

const ChannelListView = ({ channels, active, onSelect }) => {

    const isActive = channel => (active && active.key === channel.key);
    const selectChannel = channel => () => onSelect(channel);

    return (
        <ListGroup>
            {channels.map(channel =>
                <ListGroupItem key={channel.key}
                               onClick={selectChannel(channel)}
                               active={isActive(channel)}>
                    #{channel.name}
                </ListGroupItem>
            )}
        </ListGroup>
    );

};

export default ChannelListView;
