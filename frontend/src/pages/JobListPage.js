import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography } from '@mui/material';
import { Link } from 'react-router-dom';

const JobListPage = () => {
    const [jobs, setJobs] = useState([]);

    useEffect(() => {
        // Fetch all jobs
        const fetchJobs = async () => {
            try {
                const response = await axios.get('/api/jobs');
                setJobs(response.data);  // Update state with fetched jobs
            } catch (error) {
                console.error('Error fetching jobs:', error);
            }
        };

        fetchJobs();
    }, []);

    const handleDelete = async (jobId) => {
        try {
            await axios.delete(`/api/jobs/${jobId}`);
            // Use the functional form of setState to ensure state is updated correctly
            setJobs(prevJobs => prevJobs.filter(job => job.jobId !== jobId)); // Remove deleted job from the list
        } catch (error) {
            console.error('Error deleting job:', error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <Typography variant="h4" gutterBottom>
                Список задач
            </Typography>
            <Button
                variant="contained"
                color="primary"
                component={Link}
                to="/create-job"
                style={{ marginBottom: '20px' }}
            >
                Добавить новую задачу
            </Button>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Название</TableCell>
                            <TableCell>Статус</TableCell>
                            <TableCell>Действия</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {jobs.map((job) => (
                            <TableRow key={job.jobId}>
                                <TableCell>{job.name}</TableCell>
                                <TableCell>{job.status}</TableCell>
                                <TableCell>
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        component={Link}
                                        to={`/jobs/${job.jobId}`}
                                        style={{ marginRight: '10px' }}
                                    >
                                        Детали
                                    </Button>
                                    <Button
                                        variant="outlined"
                                        color="secondary"
                                        onClick={() => handleDelete(job.jobId)}
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

export default JobListPage;
