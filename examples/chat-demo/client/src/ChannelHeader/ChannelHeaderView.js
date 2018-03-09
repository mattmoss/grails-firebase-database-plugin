import React from 'react';
import { PageHeader } from 'react-bootstrap';

const ChannelHeaderView = ({ channel }) => (
    <PageHeader>
        # {channel.name}<br />
        <small>Topic: {channel.topic}</small>
    </PageHeader>
);

export default ChannelHeaderView;
