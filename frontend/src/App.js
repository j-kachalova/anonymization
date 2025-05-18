import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import RuleTablePage from "./pages/RuleTablePage";
import RuleDetailPage from "./pages/RuleDetailPage";
import AddRuleSetPage from "./pages/AddRuleSetPage";
import JobListPage from "./pages/JobListPage";
import JobDetailPage from "./pages/JobDetailPage";
import CreateJobPage from "./pages/CreateJobPage";
import UpdateJobPage from "./pages/UpdateJobPage";
import FileUploadPage from "./pages/FileUploadPage"; // Если у вас есть стили для App

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<RuleTablePage />} />
                <Route path="/rule/:id" element={<RuleDetailPage />} />
                <Route path="/add-rule-set" element={<AddRuleSetPage />} />
                <Route path="/jobs" element={<JobListPage />} />
                <Route path="/jobs/:jobId" element={<JobDetailPage />} />
                <Route path="/create-job" element={<CreateJobPage />} />
                <Route path="/jobs/:jobId/edit" element={<UpdateJobPage />} />
                <Route path="/file" element={<FileUploadPage />} />
            </Routes>
        </Router>
    );
}


export default App;
