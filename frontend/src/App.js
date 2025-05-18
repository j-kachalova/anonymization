import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import RuleSetPage from "./pages/RuleSetPage";
import RuleTablePage from "./pages/RuleTablePage";
import RuleDetailPage from "./pages/RuleDetailPage";
import AddRuleSetPage from "./pages/AddRuleSetPage"; // Если у вас есть стили для App

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<RuleSetPage />} />
                <Route path="/rules" element={<RuleTablePage />} />
                <Route path="/rule/:id" element={<RuleDetailPage />} />
                <Route path="/add-rule-set" element={<AddRuleSetPage />} />
            </Routes>
        </Router>
    );
}


export default App;
