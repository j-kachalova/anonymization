import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
    Button,
    Typography,
    Paper,
    Divider,
    List,
    ListItem,
    ListItemText
} from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';

const JobDetailPage = () => {
    const { id } = useParams();
    const [job, setJob] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchJob = async () => {
            try {
                const response = await axios.get(`/api/jobs/${id}`);
                setJob(response.data);
            } catch (error) {
                console.error('Ошибка при получении задачи:', error);
            }
        };

        fetchJob();
    }, [id]);

    if (!job) {
        return <Typography variant="h6">Загрузка...</Typography>;
    }

    return (
        <div style={{ padding: '20px' }}>
            <Paper style={{ padding: '20px' }}>
                <div style={{ marginBottom: '20px' }}>
                    <Button
                        variant="outlined"
                        color="secondary"
                        onClick={() => navigate(-1)}
                        style={{ marginRight: '10px' }}
                    >
                        Назад
                    </Button>
                    <Button
                        variant="outlined"
                        color="primary"
                        onClick={() => navigate(`/jobs/${id}/edit`)}
                    >
                        Редактировать
                    </Button>
                </div>

                <Typography variant="h4" gutterBottom>
                    Детали задачи
                </Typography>

                <Typography variant="h6">Название: {job.name}</Typography>
                <Typography variant="h6">Входной топик: {job.inputTopic}</Typography>
                <Typography variant="h6">Выходной топик: {job.outputTopic}</Typography>
                <Typography variant="h6">Статус: {job.status}</Typography>

                {job.ruleSet && (
                    <>
                        <Divider style={{ margin: '20px 0' }} />
                        <Typography variant="h6">Набор правил: {job.ruleSet.name}</Typography>
                        <List>
                            {job.ruleSet.rules?.map((rule, index) => (
                                <ListItem key={index}>
                                    <ListItemText
                                        primary={`Поле: ${rule.fieldName}`}
                                        secondary={`Метод: ${rule.method}`}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    </>
                )}
            </Paper>
        </div>
    );
};

export default JobDetailPage;
