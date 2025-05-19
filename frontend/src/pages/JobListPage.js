import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography,
    Snackbar,
    Alert
} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';

const JobListPage = () => {
    const [jobs, setJobs] = useState([]);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const navigate = useNavigate();

    useEffect(() => {
        fetchJobs();
    }, []);

    const fetchJobs = async () => {
        try {
            const response = await axios.get('/api/jobs');
            setJobs(response.data);
        } catch (error) {
            console.error('Ошибка при получении задач:', error);
            showSnackbar('Ошибка при загрузке задач', 'error');
        }
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`/api/jobs/${id}`);
            setJobs(prevJobs => prevJobs.filter(job => job.id !== id));
            showSnackbar('Задача удалена', 'success');
        } catch (error) {
            console.error('Ошибка при удалении задачи:', error);
            showSnackbar('Не удалось удалить задачу', 'error');
        }
    };

    const handleStartJob = async (id) => {
        try {
            await axios.post(`/api/jobs/${id}/start`);
            showSnackbar('Задача запущена', 'success');
            fetchJobs();
        } catch (error) {
            console.error('Ошибка при запуске задачи:', error);
            showSnackbar('Ошибка запуска задачи', 'error');
        }
    };

    const handleStopJob = async (id) => {
        try {
            await axios.post(`/api/jobs/${id}/stop`);
            showSnackbar('Задача остановлена', 'success');
            fetchJobs();
        } catch (error) {
            console.error('Ошибка при остановке задачи:', error);
            showSnackbar('Ошибка остановки задачи', 'error');
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
            <Button
                variant="outlined"
                onClick={handleGoBack}
                style={{ marginBottom: '20px', marginRight: '10px' }}
            >
                Назад
            </Button>

            <Button
                variant="contained"
                color="primary"
                component={Link}
                to="/create-job"
                style={{ marginBottom: '20px' }}
            >
                Добавить новую задачу
            </Button>

            <Typography variant="h4" gutterBottom>
                Список задач
            </Typography>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Название</TableCell>
                            <TableCell>Статус</TableCell>
                            <TableCell>Действия</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {jobs.map((job) => (
                            <TableRow key={job.id}>
                                <TableCell>{job.name}</TableCell>
                                <TableCell>{job.status}</TableCell>
                                <TableCell>
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        component={Link}
                                        to={`/jobs/${job.id}`}
                                        style={{ marginRight: '10px' }}
                                    >
                                        Детали
                                    </Button>
                                    <Button
                                        variant="outlined"
                                        color="secondary"
                                        onClick={() => handleDelete(job.id)}
                                        style={{ marginRight: '10px' }}
                                    >
                                        Удалить
                                    </Button>
                                    <Button
                                        variant="contained"
                                        color="success"
                                        onClick={() => handleStartJob(job.id)}
                                        style={{ marginRight: '10px' }}
                                    >
                                        Старт
                                    </Button>
                                    <Button
                                        variant="contained"
                                        color="warning"
                                        onClick={() => handleStopJob(job.id)}
                                    >
                                        Стоп
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

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

export default JobListPage;
