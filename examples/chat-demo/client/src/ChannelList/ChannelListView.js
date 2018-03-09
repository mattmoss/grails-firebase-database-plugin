import React from 'react';
import { ListGroup, ListGroupItem } from 'react-bootstrap';

const ChannelListView = ({ channels, active, onSelectChannel }) => {

    const selectChannel = channel => () => onSelectChannel(channel);

    return (
        <ListGroup>
            {channels.map(channel =>
                <ListGroupItem key={channel.key}
                               onClick={selectChannel(channel)}
                               active={channel.key === active.key}>
                    # {channel.name}
                </ListGroupItem>
            )}
        </ListGroup>
    );

};

export default ChannelListView;
