import React, { useState } from 'react';
import axios from 'axios';
import {
    Button,
    TextField,
    Typography,
    Paper,
    Snackbar,
    Alert
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

const FileUploadPage = () => {
    const [file, setFile] = useState(null);
    const [inputTopic, setInputTopic] = useState('');
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const navigate = useNavigate();

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleFileUpload = async () => {
        if (!file || !inputTopic) {
            setSnackbarMessage('Пожалуйста, выберите файл и укажите имя топика!');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('inputTopic', inputTopic);

        try {
            const response = await axios.post('/files/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setSnackbarMessage(response.data);
            setSnackbarSeverity('success');
            setOpenSnackbar(true);
        } catch (error) {
            console.error('Ошибка при загрузке файла:', error);
            setSnackbarMessage('Ошибка при обработке файла');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    const handleGoBack = () => {
        navigate(-1);
    };

    return (
        <div style={{ padding: '20px' }}>
            <Button
                variant="outlined"
                onClick={handleGoBack}
                style={{ marginBottom: '20px' }}
            >
                Назад
            </Button>

            <Paper style={{ padding: '20px' }}>
                <Typography variant="h4" gutterBottom>
                    Загрузить файл
                </Typography>

                <TextField
                    label="Имя Kafka-топика"
                    value={inputTopic}
                    onChange={(e) => setInputTopic(e.target.value)}
                    fullWidth
                    style={{ marginBottom: '20px' }}
                />

                <input
                    type="file"
                    onChange={handleFileChange}
                    style={{ marginBottom: '20px' }}
                />

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleFileUpload}
                >
                    Загрузить файл
                </Button>
            </Paper>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default FileUploadPage;
