import React from 'react';
import firebase from '../firebase';
import UsersListView from './UsersListView';

class UsersList extends React.Component {

    state = {
        users: []
    };

    componentDidMount() {
        const usersRef = firebase.database().ref('users');

        usersRef.on('value', snapshot => {
            let users = [];
            snapshot.forEach(data => {
                users.push({ key: data.key, displayName: data.val().displayName });
            });
            this.setState({ users });
        });
    }

    render() {
        return <UsersListView users={this.state.users} />;
    }
}

export default UsersList;