import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, TextField, CircularProgress, List, ListItem, ListItemText, Paper, Typography, Divider, Select, MenuItem, FormControl, InputLabel } from '@mui/material';

const RuleSetPage = () => {
    const [ruleSets, setRuleSets] = useState([]);
    const [newRuleSet, setNewRuleSet] = useState({
        name: '',
        description: '',
        rules: [],
    });
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchRuleSets = async () => {
            try {
                const response = await axios.get('/api/rulesets');
                setRuleSets(response.data);
            } catch (error) {
                console.error('Error fetching rule sets:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchRuleSets();
    }, []);

    const handleCreateRuleSet = async () => {
        try {
            const response = await axios.post('/api/rulesets', newRuleSet);
            setRuleSets([...ruleSets, response.data]);
            setNewRuleSet({ name: '', description: '', rules: [] });
        } catch (error) {
            console.error('Error creating rule set:', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewRuleSet((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleAddRule = () => {
        setNewRuleSet((prevState) => ({
            ...prevState,
            rules: [...prevState.rules, { fieldName: '', method: '', parameters: '' }],
        }));
    };

    const handleRuleChange = (index, e) => {
        const { name, value } = e.target;
        const updatedRules = [...newRuleSet.rules];
        updatedRules[index][name] = value;
        setNewRuleSet({ ...newRuleSet, rules: updatedRules });
    };

    const handleDeleteRule = (index) => {
        const updatedRules = newRuleSet.rules.filter((_, i) => i !== index);
        setNewRuleSet({ ...newRuleSet, rules: updatedRules });
    };

    return (
        <div style={{ padding: '20px' }}>
            <Typography variant="h4" gutterBottom>
                Загрузка правил обезличивания
            </Typography>

            {isLoading ? (
                <CircularProgress />
            ) : (
                <div>
                    <Typography variant="h5" gutterBottom>
                        Существующие наборы правил
                    </Typography>
                    <List>
                        {ruleSets.map((ruleSet) => (
                            <ListItem key={ruleSet.id} component="div" sx={{ marginBottom: '10px' }}>
                                <Paper sx={{ padding: '10px', width: '100%' }}>
                                    <Typography variant="h6">{ruleSet.name}</Typography>
                                    <Typography variant="body1">{ruleSet.description}</Typography>
                                    <List>
                                        {ruleSet.rules.map((rule, index) => (
                                            <ListItem key={index}>
                                                <ListItemText primary={`${rule.fieldName} - ${rule.method}`} />
                                            </ListItem>
                                        ))}
                                    </List>
                                </Paper>
                            </ListItem>
                        ))}
                    </List>

                    <div style={{ marginTop: '20px', padding: '20px', border: '1px solid #ddd', borderRadius: '5px', backgroundColor: '#f9f9f9' }}>
                        <Typography variant="h6" gutterBottom>
                            Создать новый набор правил
                        </Typography>
                        <TextField
                            label="Название набора правил"
                            variant="outlined"
                            fullWidth
                            value={newRuleSet.name}
                            onChange={handleInputChange}
                            name="name"
                            margin="normal"
                        />
                        <TextField
                            label="Описание набора правил"
                            variant="outlined"
                            fullWidth
                            multiline
                            rows={4}
                            value={newRuleSet.description}
                            onChange={handleInputChange}
                            name="description"
                            margin="normal"
                        />
                        <Button variant="contained" color="primary" onClick={handleAddRule} style={{ marginTop: '10px' }}>
                            Добавить правило
                        </Button>

                        {newRuleSet.rules.map((rule, index) => (
                            <div key={index} style={{ marginTop: '15px' }}>
                                <TextField
                                    label="Поле"
                                    variant="outlined"
                                    fullWidth
                                    value={rule.fieldName}
                                    onChange={(e) => handleRuleChange(index, e)}
                                    name="fieldName"
                                    margin="normal"
                                />

                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Метод</InputLabel>
                                    <Select
                                        label="Метод"
                                        value={rule.method}
                                        onChange={(e) => handleRuleChange(index, e)}
                                        name="method"
                                    >
                                        <MenuItem value="MASK">MASK</MenuItem>
                                        <MenuItem value="HASH">HASH</MenuItem>
                                        <MenuItem value="DELETE">DELETE</MenuItem>
                                        <MenuItem value="GENERALIZE">GENERALIZE</MenuItem>
                                    </Select>
                                </FormControl>

                                <TextField
                                    label="Параметры"
                                    variant="outlined"
                                    fullWidth
                                    value={rule.parameters}
                                    onChange={(e) => handleRuleChange(index, e)}
                                    name="parameters"
                                    margin="normal"
                                />
                                <Button
                                    variant="outlined"
                                    color="secondary"
                                    onClick={() => handleDeleteRule(index)}
                                    style={{ marginTop: '5px' }}
                                >
                                    Удалить правило
                                </Button>
                            </div>
                        ))}

                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleCreateRuleSet}
                            style={{ marginTop: '20px' }}
                        >
                            Создать набор правил
                        </Button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default RuleSetPage;
