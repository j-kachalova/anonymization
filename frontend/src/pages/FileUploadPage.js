import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
    Button,
    Typography,
    Paper,
    Snackbar,
    Alert,
    MenuItem,
    Select,
    InputLabel,
    FormControl
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

const FileUploadPage = () => {
    const [file, setFile] = useState(null);
    const [jobs, setJobs] = useState([]);
    const [selectedJobId, setSelectedJobId] = useState('');
    const [selectedInputTopic, setSelectedInputTopic] = useState('');
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        axios.get('/api/jobs')
            .then((res) => setJobs(res.data))
            .catch((err) => {
                console.error('Ошибка при загрузке задач:', err);
                setSnackbarMessage('Ошибка при загрузке списка задач');
                setSnackbarSeverity('error');
                setOpenSnackbar(true);
            });
    }, []);

    const handleJobChange = (event) => {
        const jobId = event.target.value;
        setSelectedJobId(jobId);
        const selectedJob = jobs.find(job => job.id === jobId);
        if (selectedJob) {
            setSelectedInputTopic(selectedJob.inputTopic);
        }
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleFileUpload = async () => {
        if (!file || !selectedInputTopic) {
            setSnackbarMessage('Пожалуйста, выберите файл и задачу!');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('inputTopic', selectedInputTopic);

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

                <FormControl fullWidth style={{ marginBottom: '20px' }}>
                    <InputLabel id="job-select-label">Выберите задачу</InputLabel>
                    <Select
                        labelId="job-select-label"
                        value={selectedJobId}
                        onChange={handleJobChange}
                    >
                        {jobs.map((job) => (
                            <MenuItem key={job.id} value={job.id}>
                                {job.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

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
