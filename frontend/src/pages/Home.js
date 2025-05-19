import React from 'react';
import { Link } from 'react-router-dom';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';

function Home() {
    return (
        <div style={{ padding: '2rem' }}>
            <h1></h1>
            <Stack direction="column" spacing={2}>
                <Button variant="contained" color="primary" component={Link} to="/rules">
                    Правила обезличивания
                </Button>
                <Button variant="contained" color="primary" component={Link} to="/jobs">
                    Задания
                </Button>
                <Button variant="contained" color="primary" component={Link} to="/file">
                    Отправить данные
                </Button>

            </Stack>
        </div>
    );
}

export default Home;