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
import { useNavigate } from 'react-router-dom';

const CreateJobPage = () => {
    const [name, setName] = useState('');
    const [inputTopic, setInputTopic] = useState('');
    const [ruleSetId, setRuleSetId] = useState('');
    const [ruleSets, setRuleSets] = useState([]);
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
    const navigate = useNavigate();

    useEffect(() => {
        const fetchRuleSets = async () => {
            try {
                const response = await axios.get('/api/rulesets');
                setRuleSets(response.data);
            } catch (error) {
                console.error('Ошибка при загрузке наборов правил:', error);
            }
        };

        fetchRuleSets();
    }, []);

    const handleCreateJob = async () => {
        if (!name || !inputTopic || !ruleSetId) {
            setSnackbar({ open: true, message: 'Заполните все поля', severity: 'error' });
            return;
        }

        const newJob = {
            name,
            inputTopic,
            ruleSet: {
                id: ruleSetId
            }
        };

        try {
            await axios.post('/api/jobs', newJob);
            setSnackbar({ open: true, message: 'Задача успешно создана', severity: 'success' });
            setTimeout(() => navigate('/jobs'), 1000);
        } catch (error) {
            console.error('Ошибка при создании задачи:', error);
            setSnackbar({ open: true, message: 'Ошибка при создании задачи', severity: 'error' });
        }
    };

    const handleGoBack = () => {
        navigate(-1);
    };

    return (
        <div style={{ padding: '20px' }}>
            <Paper style={{ padding: '20px' }}>
                <Button
                    variant="outlined"
                    color="secondary"
                    onClick={handleGoBack}
                    style={{ marginBottom: '20px' }}
                >
                    Назад
                </Button>

                <Typography variant="h4" gutterBottom>
                    Создать новую задачу
                </Typography>

                <TextField
                    label="Название"
                    variant="outlined"
                    fullWidth
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <TextField
                    label="Входной топик для данных"
                    variant="outlined"
                    fullWidth
                    value={inputTopic}
                    onChange={(e) => setInputTopic(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <FormControl fullWidth variant="outlined" style={{ marginBottom: '20px' }}>
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
                    onClick={handleCreateJob}
                >
                    Создать задачу
                </Button>
            </Paper>

            <Snackbar
                open={snackbar.open}
                autoHideDuration={4000}
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert
                    onClose={() => setSnackbar({ ...snackbar, open: false })}
                    severity={snackbar.severity}
                    sx={{ width: '100%' }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default CreateJobPage;
