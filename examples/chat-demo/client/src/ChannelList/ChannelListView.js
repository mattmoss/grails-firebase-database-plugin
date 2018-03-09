import React from 'react';
import { ListGroup, ListGroupItem } from 'react-bootstrap';

const ChannelListView = ({ channels }) => (
    <ListGroup>
        {channels.map(({ key, name, topic }) =>
            <ListGroupItem key={key} header={'#' + name}></ListGroupItem>
        )}
    </ListGroup>
);

export default ChannelListView;
