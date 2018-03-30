import React from 'react';
import { ListGroup, ListGroupItem } from 'react-bootstrap';

const UsersList = ({ users }) => {

    return (
        <ListGroup>
            {users.map(user =>
                <ListGroupItem key={user.key} header={user.displayName}>
                    <small>Status: {user.status ? user.status : 'unknown'}</small>
                </ListGroupItem>
            )}
        </ListGroup>
    );

};

export default UsersList;