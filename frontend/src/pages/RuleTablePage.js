import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const RuleTablePage = () => {
    const [ruleSets, setRuleSets] = useState([]);
    const navigate  = useNavigate();

    // Загрузка всех наборов правил
    useEffect(() => {
        const fetchRuleSets = async () => {
            try {
                const response = await axios.get('/api/rulesets');
                setRuleSets(response.data);
            } catch (error) {
                console.error('Error fetching rule sets:', error);
            }
        };

        fetchRuleSets();
    }, []);

    // Удаление набора правил
    const handleDelete = async (id) => {
        try {
            await axios.delete(`/api/rulesets/${id}`);
            setRuleSets(ruleSets.filter(ruleSet => ruleSet.id !== id)); // Удаление из состояния после успешного запроса
        } catch (error) {
            console.error('Error deleting rule set:', error);
        }
    };

    // Переход на страницу с полным описанием правила
    const handleViewDetail = (id) => {
        navigate(`/rule/${id}`);
    };

    // Переход на страницу добавления нового набора правил
    const handleAddRuleSet = () => {
        navigate('/add-rule-set'); // Переход на страницу добавления нового набора правил
    };

    return (
        <div style={{ padding: '20px' }}>
            {/* Кнопка "Добавить набор правил" */}
            <Button
                variant="contained"
                color="primary"
                onClick={handleAddRuleSet}
                style={{ marginBottom: '20px' }}
            >
                Добавить набор правил
            </Button>

            <Typography variant="h4" gutterBottom>
                Таблица наборов правил
            </Typography>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Название</TableCell>
                            <TableCell>Описание</TableCell>
                            <TableCell>Действия</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {ruleSets.map((ruleSet) => (
                            <TableRow key={ruleSet.id}>
                                <TableCell>{ruleSet.name}</TableCell>
                                <TableCell>{ruleSet.description}</TableCell>
                                <TableCell>
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        onClick={() => handleViewDetail(ruleSet.id)}
                                        style={{ marginRight: '10px' }}
                                    >
                                        Открыть
                                    </Button>
                                    <Button
                                        variant="outlined"
                                        color="secondary"
                                        onClick={() => handleDelete(ruleSet.id)}
                                    >
                                        Удалить
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default RuleTablePage;
