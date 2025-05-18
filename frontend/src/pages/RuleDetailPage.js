import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Typography, Paper, List, ListItem, ListItemText, Divider, Button } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';

const RuleDetailPage = () => {
    const { id } = useParams(); // Получаем параметр id из URL
    const [ruleSet, setRuleSet] = useState(null);
    const navigate = useNavigate(); // Для навигации назад

    useEffect(() => {
        // Запрос данных о наборе правил с сервера по id
        const fetchRuleSet = async () => {
            try {
                const response = await axios.get(`/api/rulesets/${id}`);
                setRuleSet(response.data);
            } catch (error) {
                console.error('Error fetching rule set detail:', error);
            }
        };

        fetchRuleSet();
    }, [id]);

    // Удаление набора правил
    const handleDelete = async () => {
        try {
            await axios.delete(`/api/rulesets/${id}`);
            navigate('/'); // Перенаправление на главную страницу после удаления
        } catch (error) {
            console.error('Error deleting rule set:', error);
        }
    };

    // Если данные не загружены, показываем индикатор загрузки
    if (!ruleSet) {
        return <Typography variant="h6">Загрузка...</Typography>;
    }

    return (
        <div style={{ padding: '20px' }}>
            {/* Кнопки "Назад" и "Удалить" */}
            <div style={{ marginBottom: '20px' }}>
                <Button
                    variant="contained"
                    color="secondary"
                    onClick={() => navigate(-1)} // Навигация назад
                    style={{ marginRight: '10px' }}
                >
                    Назад
                </Button>
            </div>

            <Typography variant="h4" gutterBottom>
                {ruleSet.name} - Полная информация
            </Typography>

            <Paper style={{ padding: '20px' }}>
                <Typography variant="h6">Описание:</Typography>
                <Typography variant="body1" paragraph>
                    {ruleSet.description}
                </Typography>

                <Divider style={{ margin: '20px 0' }} />

                <Typography variant="h6">Правила обезличивания:</Typography>
                <List>
                    {ruleSet.rules.map((rule, index) => (
                        <ListItem key={index}>
                            <ListItemText
                                primary={`Поле: ${rule.fieldName}`}
                                secondary={`Метод: ${rule.method} | Параметры: ${rule.parameters}`}
                            />
                        </ListItem>
                    ))}
                </List>
            </Paper>
        </div>
    );
};

export default RuleDetailPage;
