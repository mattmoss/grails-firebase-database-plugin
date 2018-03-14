import React from 'react';
import { Button, FormControl, FormGroup, InputGroup } from 'react-bootstrap';

const ChannelInputView = ({ channel, message, onChange, onSubmit }) => {

    const editMessage = event => onChange(event.target.value);
    const sendMessage = event => {
        onSubmit();
        event.preventDefault();
    };

    return (
        <form onSubmit={sendMessage}>
            <FormGroup>
                <InputGroup>
                    <FormControl type="text"
                                 value={message}
                                 placeholder={'Message #' + channel.name}
                                 onChange={editMessage}>
                    </FormControl>
                    <InputGroup.Button>
                        <Button onClick={sendMessage}
                                disabled={message.trim() === ''}>
                            Send
                        </Button>
                    </InputGroup.Button>
                </InputGroup>
            </FormGroup>
        </form>
    );
};

export default ChannelInputView;