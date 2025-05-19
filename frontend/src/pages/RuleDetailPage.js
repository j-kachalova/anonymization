import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Typography, Paper, List, ListItem, ListItemText, Divider, Button } from '@mui/material';
import { useParams, useNavigate } from 'react-router-dom';

const RuleDetailPage = () => {
    const { id } = useParams();
    const [ruleSet, setRuleSet] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
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

    const handleDelete = async () => {
        try {
            await axios.delete(`/api/rulesets/${id}`);
            navigate('/');
        } catch (error) {
            console.error('Error deleting rule set:', error);
        }
    };

    if (!ruleSet) {
        return <Typography variant="h6">Загрузка...</Typography>;
    }

    return (
        <div style={{ padding: '20px' }}>
            <div style={{ marginBottom: '20px' }}>
                <Button
                    variant="contained"
                    color="secondary"
                    onClick={() => navigate(-1)}
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
                                secondary={`Метод: ${rule.method}`}
                            />
                        </ListItem>
                    ))}
                </List>
            </Paper>
        </div>
    );
};

export default RuleDetailPage;
