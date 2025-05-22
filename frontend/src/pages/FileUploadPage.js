import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import {
    Button, Typography, Paper, Snackbar, Alert,
    MenuItem, Select, InputLabel, FormControl,
    TextField, Box, Stack, RadioGroup, FormControlLabel, Radio
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

const FileUploadPage = () => {
    const [mode, setMode] = useState('file');
    const [file, setFile] = useState(null);
    const [jobs, setJobs] = useState([]);
    const [selectedJobId, setSelectedJobId] = useState('');
    const [selectedInputTopic, setSelectedInputTopic] = useState('');
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const fileInputRef = useRef(null);

    const [formData, setFormData] = useState({
        birthDate: '', birthPlace: '', address: '',
        phone: '', email: '', inn: '', snils: '', card: ''
    });
    const [passportSeries, setPassportSeries] = useState('');
    const [passportNumber, setPassportNumber] = useState('');
    const [errors, setErrors] = useState({});

    const navigate = useNavigate();

    useEffect(() => {
        axios.get('/api/jobs')
            .then((res) => {
                const runningJobs = res.data.filter(job => job.status === 'RUNNING');
                setJobs(runningJobs);
            })
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

    const resetFile = () => {
        setFile(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = null;
        }
    };

    const resetForm = () => {
        setFormData({
            birthDate: '', birthPlace: '', address: '',
            phone: '', email: '', inn: '', snils: '', card: ''
        });
        setPassportSeries('');
        setPassportNumber('');
        setErrors({});
    };

    const handleUploadFile = async () => {
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
                headers: { 'Content-Type': 'multipart/form-data' },
            });
            setSnackbarMessage(response.data);
            setSnackbarSeverity('success');
            resetFile();
        } catch (error) {
            setSnackbarMessage('Ошибка при загрузке файла');
            setSnackbarSeverity('error');
        } finally {
            setOpenSnackbar(true);
        }
    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handlePassportSeriesChange = (e) => {
        setPassportSeries(e.target.value.replace(/\D/g, '').slice(0, 4));
    };

    const handlePassportNumberChange = (e) => {
        setPassportNumber(e.target.value.replace(/\D/g, '').slice(0, 6));
    };

    const validate = () => {
        const newErrors = {};
        if (!/^\d{2}\.\d{2}\.\d{4}$/.test(formData.birthDate)) {
            newErrors.birthDate = 'Дата в формате ДД.ММ.ГГГГ';
        }
        if (!/^[А-Яа-я\-\s]{2,}$/.test(formData.birthPlace)) {
            newErrors.birthPlace = 'Кириллица, не короче 2 символов';
        }
        if (!/^\d{4}$/.test(passportSeries)) {
            newErrors.passportSeries = '4 цифры';
        }
        if (!/^\d{6}$/.test(passportNumber)) {
            newErrors.passportNumber = '6 цифр';
        }
        if (!/^.{5,}$/.test(formData.address)) {
            newErrors.address = 'Мин. 5 символов';
        }
        if (!/^(89|\+79)\d{9}$/.test(formData.phone)) {
            newErrors.phone = 'Некорректный номер';
        }
        if (!/^[\w.-]+@(?:mail\.ru|gmail\.com|yandex\.ru|bk\.ru|outlook\.com|icloud\.com|rambler\.ru)$/.test(formData.email)) {
            newErrors.email = 'Разрешённые домены';
        }
        if (!/^\d{12}$/.test(formData.inn)) {
            newErrors.inn = 'ИНН из 12 цифр';
        }
        if (!/^\d{3}-\d{3}-\d{3}\s\d{2}$/.test(formData.snils)) {
            newErrors.snils = 'Формат: 123-456-789 00';
        }
        if (!/^\d{4}([ -]?\d{4}){3}$/.test(formData.card)) {
            newErrors.card = '16 цифр';
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmitForm = async (e) => {
        e.preventDefault();
        if (!selectedInputTopic) {
            setSnackbarMessage('Выберите задачу');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
            return;
        }
        if (!validate()) return;

        const dataToSend = {
            ...formData,
            passport: `${passportSeries} ${passportNumber}`
        };

        try {
            const res = await axios.post(`/api/anonymization/anonymizeData?inputTopic=${selectedInputTopic}`, dataToSend);
            setSnackbarMessage(res.data);
            setSnackbarSeverity('success');
            resetForm();
        } catch (err) {
            setSnackbarMessage('Ошибка при отправке формы: ' + err.message);
            setSnackbarSeverity('error');
        } finally {
            setOpenSnackbar(true);
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Box sx={{ p: 4 }}>
            <Button variant="outlined" onClick={() => navigate(-1)} sx={{ mb: 2 }}>Назад</Button>

            <Paper sx={{ p: 3, mb: 3 }}>
                <Typography variant="h5" gutterBottom>Выберите режим</Typography>
                <RadioGroup row value={mode} onChange={(e) => setMode(e.target.value)}>
                    <FormControlLabel value="file" control={<Radio />} label="Загрузить файл" />
                    <FormControlLabel value="form" control={<Radio />} label="Заполнить форму" />
                </RadioGroup>

                <FormControl fullWidth sx={{ mt: 2 }}>
                    <InputLabel id="job-select-label">Выберите задачу</InputLabel>
                    <Select
                        labelId="job-select-label"
                        value={selectedJobId}
                        onChange={handleJobChange}
                    >
                        {jobs.map((job) => (
                            <MenuItem key={job.id} value={job.id}>{job.name}</MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Paper>

            {mode === 'file' && (
                <Paper sx={{ p: 3 }}>
                    <Typography variant="h6" gutterBottom>Загрузка файла</Typography>
                    <input
                        type="file"
                        onChange={handleFileChange}
                        ref={fileInputRef}
                        style={{ marginBottom: '20px' }}
                    />
                    <Button variant="contained" color="primary" onClick={handleUploadFile}>
                        Загрузить файл
                    </Button>
                </Paper>
            )}

            {mode === 'form' && (
                <Paper sx={{ p: 3 }}>
                    <Typography variant="h6" gutterBottom>Заполните форму</Typography>
                    <form onSubmit={handleSubmitForm}>
                        <Stack spacing={2}>
                            <TextField name="birthDate" label="Дата рождения (ДД.ММ.ГГГГ)"
                                       value={formData.birthDate} onChange={handleChange} required
                                       error={!!errors.birthDate} helperText={errors.birthDate} />
                            <TextField name="birthPlace" label="Место рождения"
                                       value={formData.birthPlace} onChange={handleChange} required
                                       error={!!errors.birthPlace} helperText={errors.birthPlace} />
                            <Stack direction="row" spacing={2}>
                                <TextField label="Серия паспорта"
                                           value={passportSeries} onChange={handlePassportSeriesChange}
                                           inputProps={{ maxLength: 4 }} required
                                           error={!!errors.passportSeries} helperText={errors.passportSeries} />
                                <TextField label="Номер паспорта"
                                           value={passportNumber} onChange={handlePassportNumberChange}
                                           inputProps={{ maxLength: 6 }} required
                                           error={!!errors.passportNumber} helperText={errors.passportNumber} />
                            </Stack>
                            <TextField name="address" label="Адрес" value={formData.address}
                                       onChange={handleChange} required error={!!errors.address}
                                       helperText={errors.address} />
                            <TextField name="phone" label="Телефон" value={formData.phone}
                                       onChange={handleChange} required error={!!errors.phone}
                                       helperText={errors.phone} />
                            <TextField name="email" label="Email" value={formData.email}
                                       onChange={handleChange} required error={!!errors.email}
                                       helperText={errors.email} />
                            <TextField name="inn" label="ИНН" value={formData.inn}
                                       onChange={handleChange} required error={!!errors.inn}
                                       helperText={errors.inn} />
                            <TextField name="snils" label="СНИЛС" value={formData.snils}
                                       onChange={handleChange} required error={!!errors.snils}
                                       helperText={errors.snils} />
                            <TextField name="card" label="Номер карты" value={formData.card}
                                       onChange={handleChange} required error={!!errors.card}
                                       helperText={errors.card} />
                            <Button type="submit" variant="contained" color="primary">
                                Отправить форму
                            </Button>
                        </Stack>
                    </form>
                </Paper>
            )}

            <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={snackbarSeverity}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default FileUploadPage;
