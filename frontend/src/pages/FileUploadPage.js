import React, { useState } from 'react';
import axios from 'axios';
import { Button, TextField, Typography, Paper, Snackbar, Alert } from '@mui/material';

const FileUploadPage = () => {
    const [file, setFile] = useState(null);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success'); // 'success' or 'error'
    const [openSnackbar, setOpenSnackbar] = useState(false);

    // Обработчик изменения файла
    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    // Обработчик отправки файла
    const handleFileUpload = async () => {
        if (!file) {
            setSnackbarMessage('Пожалуйста, выберите файл для загрузки!');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('/files/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setSnackbarMessage(response.data); // Сообщение от сервера
            setSnackbarSeverity('success');
            setOpenSnackbar(true);
        } catch (error) {
            console.error('Error uploading file:', error);
            setSnackbarMessage('Ошибка при обработке файла');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
        }
    };

    // Закрытие Snackbar
    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <div style={{ padding: '20px' }}>
            <Paper style={{ padding: '20px' }}>
                <Typography variant="h4" gutterBottom>
                    Загрузить файл
                </Typography>
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

            {/* Snackbar для уведомлений */}
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
