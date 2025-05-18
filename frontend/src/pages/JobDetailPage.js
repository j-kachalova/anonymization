import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Typography, Paper } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';

const JobDetailPage = () => {
    const { jobId } = useParams();
    const [job, setJob] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const fetchJob = async () => {
            const response = await axios.get(`/api/jobs/${jobId}`);
            setJob(response.data);
        };
        fetchJob();
    }, [jobId]);

    return (
        <div style={{ padding: '20px' }}>
            <Paper style={{ padding: '20px' }}>
                <Typography variant="h4" gutterBottom>
                    Детали задачи
                </Typography>
                <Typography variant="h6">Название: {job.name}</Typography>
                <Typography variant="h6">Входной топик: {job.inputTopic}</Typography>
                <Typography variant="h6">Выходной топик: {job.outputTopic}</Typography>
                <Typography variant="h6">Статус: {job.status}</Typography>
                <Typography variant="h6">Расписание: {job.schedule}</Typography>
                <Button
                    variant="outlined"
                    color="primary"
                    onClick={() => navigate(`/jobs/${jobId}/edit`)}
                >
                    Редактировать
                </Button>
            </Paper>
        </div>
    );
};

export default JobDetailPage;
