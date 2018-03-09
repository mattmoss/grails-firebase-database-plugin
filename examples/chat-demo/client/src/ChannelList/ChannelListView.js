import React from 'react';
import { ListGroup, ListGroupItem } from 'react-bootstrap';

const ChannelListView = ({ channels }) => (
    <ListGroup>
        {channels.map(({ key, name }) =>
            <ListGroupItem key={key}># {name}</ListGroupItem>
        )}
    </ListGroup>
);

export default ChannelListView;
