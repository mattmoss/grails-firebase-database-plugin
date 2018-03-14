import React from 'react';
import { PageHeader } from 'react-bootstrap';

const ChannelHeaderView = ({ channel }) => (
    <PageHeader>
        <strong>#{channel.name}</strong> [ <i>{channel.topic}</i> ]
    </PageHeader>
);

export default ChannelHeaderView;
