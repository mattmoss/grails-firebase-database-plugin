import React from 'react';
import { Table } from 'react-bootstrap';

const ChannelMessagesView = ({messages}) => {

    return (
        <div id="chatMessages">
            <Table>
                <tbody>
                    {messages.map(message =>
                        <tr key={message.key}>
                            <td className="col-md-2 bg-info">{message.author}</td>
                            <td className="col-md-8">{message.message}</td>
                            <td className="col-md-2 text-right bg-success">{message.timestamp}</td>
                        </tr>
                    )}
                </tbody>
            </Table>
        </div>
    );
};

export default ChannelMessagesView;
