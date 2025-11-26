class BankAPI {
    constructor() {
        this.baseURL = '/api';
    }

    async login(username, password) {
        const response = await fetch(`${this.baseURL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Login failed');
        }

        const user = await response.json();
        sessionStorage.setItem('currentUser', JSON.stringify(user));
        return user;
    }

    async logout() {
        await fetch(`${this.baseURL}/logout`, {
            method: 'POST'
        });
        sessionStorage.removeItem('currentUser');
    }

    getCurrentUser() {
        const userStr = sessionStorage.getItem('currentUser');
        return userStr ? JSON.parse(userStr) : null;
    }

    async makeTransaction(transactionData) {
        const response = await fetch(`${this.baseURL}/transaction`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(transactionData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Transaction failed');
        }

        return await response.json();
    }

    async createUser(userData) {
        const response = await fetch(`${this.baseURL}/admin/create-user`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Failed to create user');
        }

        return await response.json();
    }

    async updateUser(userId, userData) {
        const response = await fetch(`${this.baseURL}/admin/users/${userId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Failed to update user');
        }

        return await response.json();
    }

    async searchUsers(query) {
        const response = await fetch(`${this.baseURL}/search?query=${encodeURIComponent(query)}`);

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Search failed');
        }

        return await response.json();
    }
}