import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
    Table, TableBody, TableCell, TableContainer,
    TableHead, TableRow, Paper, Typography, Box, Button,
    Checkbox, FormControlLabel, FormGroup
} from '@mui/material';
import { Link } from "react-router-dom";
import * as XLSX from 'xlsx';

const allColumns = [
    { key: 'id', label: 'ID' },
    { key: 'birthDate', label: 'Дата рождения' },
    { key: 'birthPlace', label: 'Место рождения' },
    { key: 'passport', label: 'Паспорт' },
    { key: 'address', label: 'Адрес' },
    { key: 'phone', label: 'Телефон' },
    { key: 'email', label: 'Email' },
    { key: 'inn', label: 'ИНН' },
    { key: 'snils', label: 'СНИЛС' },
    { key: 'card', label: 'Карта' },
];

const AnonymizedPage = () => {
    const [data, setData] = useState([]);
    const [error, setError] = useState('');
    const [visibleColumns, setVisibleColumns] = useState(() => {
        // По умолчанию все поля отображаются
        const initial = {};
        allColumns.forEach(col => initial[col.key] = true);
        return initial;
    });

    useEffect(() => {
        axios.get('/api/anonymization/all-anonymized')
            .then((res) => setData(res.data))
            .catch((err) => setError('Ошибка при загрузке данных: ' + err.message));
    }, []);

    const handleExport = () => {
        const exportData = data.map(row =>
            Object.fromEntries(
                Object.entries(row).filter(([key]) => visibleColumns[key])
            )
        );
        const worksheet = XLSX.utils.json_to_sheet(exportData);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, 'Anonymized Data');
        XLSX.writeFile(workbook, 'anonymized_data.xlsx');
    };

    const handleToggleColumn = (key) => {
        setVisibleColumns(prev => ({ ...prev, [key]: !prev[key] }));
    };

    return (
        <Box sx={{ p: 4 }}>
            <Typography variant="h5" gutterBottom>Обезличенные данные</Typography>
            {error && <Typography color="error">{error}</Typography>}

            <Box sx={{ mb: 2, display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                <Button variant="outlined" component={Link} to="/">Назад</Button>
                <Button variant="contained" color="primary" onClick={handleExport}>
                    Выгрузить в Excel
                </Button>
            </Box>

            <Box sx={{ mb: 2 }}>
                <Typography variant="subtitle1">Выберите отображаемые поля:</Typography>
                <FormGroup row>
                    {allColumns.map(col => (
                        <FormControlLabel
                            key={col.key}
                            control={
                                <Checkbox
                                    checked={visibleColumns[col.key]}
                                    onChange={() => handleToggleColumn(col.key)}
                                />
                            }
                            label={col.label}
                        />
                    ))}
                </FormGroup>
            </Box>

            <TableContainer component={Paper}>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            {allColumns
                                .filter(col => visibleColumns[col.key])
                                .map(col => (
                                    <TableCell key={col.key}>{col.label}</TableCell>
                                ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row) => (
                            <TableRow key={row.id}>
                                {allColumns
                                    .filter(col => visibleColumns[col.key])
                                    .map(col => (
                                        <TableCell key={col.key}>{row[col.key]}</TableCell>
                                    ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default AnonymizedPage;
