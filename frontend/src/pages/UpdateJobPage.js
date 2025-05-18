import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, TextField, FormControl, InputLabel, Select, MenuItem, Typography, Paper } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';

const UpdateJobPage = () => {
    const { jobId } = useParams();
    const [job, setJob] = useState({});
    const [name, setName] = useState('');
    const [inputTopic, setInputTopic] = useState('');
    const [outputTopic, setOutputTopic] = useState('');
    const [ruleSet, setRuleSet] = useState('');
    const [schedule, setSchedule] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchJob = async () => {
            const response = await axios.get(`/api/jobs/${jobId}`);
            setJob(response.data);
            setName(response.data.name);
            setInputTopic(response.data.inputTopic);
            setOutputTopic(response.data.outputTopic);
            setRuleSet(response.data.ruleSet);
            setSchedule(response.data.schedule);
        };
        fetchJob();
    }, [jobId]);

    const handleUpdateJob = async () => {
        const updatedJob = { name, inputTopic, outputTopic, ruleSet, schedule };
        try {
            await axios.put(`/api/jobs/${jobId}`, updatedJob);
            navigate('/jobs'); // Redirect after update
        } catch (error) {
            console.error('Error updating job:', error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <Paper style={{ padding: '20px' }}>
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
                />
                <TextField
                    label="Входной топик"
                    variant="outlined"
                    fullWidth
                    value={inputTopic}
                    onChange={(e) => setInputTopic(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />
                <TextField
                    label="Выходной топик"
                    variant="outlined"
                    fullWidth
                    value={outputTopic}
                    onChange={(e) => setOutputTopic(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />
                <FormControl fullWidth variant="outlined" style={{ marginBottom: '20px' }}>
                    <InputLabel>Правила</InputLabel>
                    <Select
                        value={ruleSet}
                        onChange={(e) => setRuleSet(e.target.value)}
                        label="Правила"
                    >
                        <MenuItem value="rule1">Правило 1</MenuItem>
                        <MenuItem value="rule2">Правило 2</MenuItem>
                        <MenuItem value="rule3">Правило 3</MenuItem>
                    </Select>
                </FormControl>
                <TextField
                    label="Расписание (по cron)"
                    variant="outlined"
                    fullWidth
                    value={schedule}
                    onChange={(e) => setSchedule(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleUpdateJob}
                >
                    Обновить задачу
                </Button>
            </Paper>
        </div>
    );
};

export default UpdateJobPage;
