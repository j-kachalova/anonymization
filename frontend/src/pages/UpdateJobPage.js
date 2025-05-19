import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
    Button,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
    Paper,
    Snackbar,
    Alert
} from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';

const UpdateJobPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [name, setName] = useState('');
    const [inputTopic, setInputTopic] = useState('');
    const [outputTopic, setOutputTopic] = useState('');
    const [ruleSetId, setRuleSetId] = useState('');
    const [ruleSets, setRuleSets] = useState([]);

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');

    // Загрузка текущей задачи
    useEffect(() => {
        const fetchJob = async () => {
            try {
                const response = await axios.get(`/api/jobs/${id}`);
                const job = response.data;
                setName(job.name);
                setInputTopic(job.inputTopic);
                setOutputTopic(job.outputTopic);
                setRuleSetId(job.ruleSet?.id || '');
            } catch (error) {
                console.error('Ошибка при получении задачи:', error);
                showSnackbar('Не удалось загрузить данные задачи', 'error');
            }
        };

        fetchJob();
    }, [id]);

    // Загрузка доступных наборов правил
    useEffect(() => {
        const fetchRuleSets = async () => {
            try {
                const response = await axios.get('/api/rulesets');
                setRuleSets(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке наборов правил:', error);
                showSnackbar('Не удалось загрузить наборы правил', 'error');
            }
        };

        fetchRuleSets();
    }, []);

    const handleUpdateJob = async () => {
        if (!name || !inputTopic || !outputTopic || !ruleSetId) {
            showSnackbar('Пожалуйста, заполните все обязательные поля', 'error');
            return;
        }

        const updatedJob = {
            name,
            inputTopic,
            outputTopic,
            ruleSetId
        };

        try {
            await axios.put(`/api/jobs/${id}`, updatedJob);
            showSnackbar('Задача успешно обновлена', 'success');
            setTimeout(() => navigate('/jobs'), 1500);
        } catch (error) {
            console.error('Ошибка при обновлении задачи:', error);
            showSnackbar('Ошибка при обновлении задачи', 'error');
        }
    };

    const handleGoBack = () => {
        navigate(-1);
    };

    const showSnackbar = (message, severity = 'success') => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };

    const handleCloseSnackbar = () => {
        setSnackbarOpen(false);
    };

    return (
        <div style={{ padding: '20px' }}>
            <Paper style={{ padding: '20px' }}>
                <div style={{ marginBottom: '20px' }}>
                    <Button
                        variant="outlined"
                        color="secondary"
                        onClick={handleGoBack}
                        style={{ marginRight: '10px' }}
                    >
                        Назад
                    </Button>
                </div>

                <Typography variant="h4" gutterBottom>
                    Обновить задачу
                </Typography>

                <TextField
                    label="Название"
                    variant="outlined"
                    fullWidth
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    style={{ marginBottom: '20px' }}
                    required
                />

                <TextField
                    label="Входной топик"
                    variant="outlined"
                    fullWidth
                    value={inputTopic}
                    onChange={(e) => setInputTopic(e.target.value)}
                    style={{ marginBottom: '20px' }}
                    required
                />

                <TextField
                    label="Выходной топик"
                    variant="outlined"
                    fullWidth
                    value={outputTopic}
                    onChange={(e) => setOutputTopic(e.target.value)}
                    style={{ marginBottom: '20px' }}
                    required
                />

                <FormControl fullWidth variant="outlined" style={{ marginBottom: '20px' }} required>
                    <InputLabel>Набор правил</InputLabel>
                    <Select
                        value={ruleSetId}
                        onChange={(e) => setRuleSetId(e.target.value)}
                        label="Набор правил"
                    >
                        {ruleSets.map((rs) => (
                            <MenuItem key={rs.id} value={rs.id}>
                                {rs.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleUpdateJob}
                >
                    Обновить задачу
                </Button>
            </Paper>

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={4000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity} sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default UpdateJobPage;
