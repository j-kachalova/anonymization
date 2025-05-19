import React, { useState } from 'react';
import axios from 'axios';
import {
    Button, TextField, Typography, Paper, Grid,
    Snackbar, Alert, MenuItem, Select, FormControl, InputLabel
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

const AddRuleSetPage = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [rules, setRules] = useState([{ fieldName: '', method: '' }]);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');
    const navigate = useNavigate();

    const fieldOptions = ['birthDate', 'birthPlace', 'passport', 'address', 'phone', 'email', 'inn', 'snils', 'card'];
    const methodOptions = ['MASK', 'HASH', 'DELETE', 'GENERALIZE'];

    const handleAddRule = () => {
        setRules([...rules, { fieldName: '', method: '' }]);
    };

    const handleRuleChange = (index, field, value) => {
        const newRules = [...rules];
        newRules[index][field] = value;
        setRules(newRules);
    };

    const handleDeleteRule = (index) => {
        const newRules = rules.filter((_, i) => i !== index);
        setRules(newRules);
    };

    const validateForm = () => {
        return rules.some(rule => rule.fieldName || rule.method);
    };

    const handleAddRuleSet = async () => {
        if (!validateForm()) {
            setSnackbarMessage('Правила не могут быть пустыми! Заполните хотя бы одно поле в каждом правиле.');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
            return;
        }

        try {
            const newRuleSet = { name, description, rules };
            await axios.post('/api/rulesets', newRuleSet);
            setSnackbarMessage('Набор правил успешно добавлен!');
            setSnackbarSeverity('success');
            setOpenSnackbar(true);
            navigate('/rules');
        } catch (error) {
            console.error('Error adding rule set:', error);
            setSnackbarMessage('Ошибка при добавлении набора правил.');
            setSnackbarSeverity('error');
            setOpenSnackbar(true);
        }
    };

    const handleGoBack = () => {
        navigate(-1);
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
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
                    Добавить новый набор правил
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
                    label="Описание"
                    variant="outlined"
                    fullWidth
                    multiline
                    rows={4}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <Typography variant="h6" gutterBottom>
                    Правила анонимизации
                </Typography>

                {rules.map((rule, index) => (
                    <div key={index} style={{ marginBottom: '20px' }}>
                        <Grid container spacing={2}>
                            <Grid item xs={6}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Поле</InputLabel>
                                    <Select
                                        value={rule.fieldName}
                                        onChange={(e) => handleRuleChange(index, 'fieldName', e.target.value)}
                                        label="Поле"
                                    >
                                        {fieldOptions.map((field) => (
                                            <MenuItem key={field} value={field}>
                                                {field}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={6}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Метод</InputLabel>
                                    <Select
                                        value={rule.method}
                                        onChange={(e) => handleRuleChange(index, 'method', e.target.value)}
                                        label="Метод"
                                    >
                                        {methodOptions.map((method) => (
                                            <MenuItem key={method} value={method}>
                                                {method}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Grid>
                        </Grid>
                        <Button
                            variant="outlined"
                            color="secondary"
                            onClick={() => handleDeleteRule(index)}
                            style={{ marginTop: '10px' }}
                        >
                            Удалить правило
                        </Button>
                    </div>
                ))}

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleAddRule}
                    style={{ margin: '20px 40px 20px 0px' }}
                >
                    Добавить правило
                </Button>

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleAddRuleSet}
                >
                    Добавить набор правил
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

export default AddRuleSetPage;
