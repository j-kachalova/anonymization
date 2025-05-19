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
import FileUploadPage from "./pages/FileUploadPage";
import Home from "./pages/Home";
import AnonymizedPage from "./pages/AnonymizedPage";
import LinkTablePage from "./pages/LinkTablePage";
import LinkDetailsPage from "./pages/LinkDetailsPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/rules" element={<RuleTablePage />} />
                <Route path="/rule/:id" element={<RuleDetailPage />} />
                <Route path="/add-rule-set" element={<AddRuleSetPage />} />
                <Route path="/jobs" element={<JobListPage />} />
                <Route path="/jobs/:id" element={<JobDetailPage />} />
                <Route path="/create-job" element={<CreateJobPage />} />
                <Route path="/jobs/:id/edit" element={<UpdateJobPage />} />
                <Route path="/file" element={<FileUploadPage />} />
                <Route path="/anonymized" element={<AnonymizedPage />} />
                <Route path="/links" element={<LinkTablePage />} />
                <Route path="/link-details/:id" element={<LinkDetailsPage />} />
            </Routes>
        </Router>
    );
}


export default App;
