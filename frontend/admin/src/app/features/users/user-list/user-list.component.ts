import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService, AdminUser } from '../user.service';

@Component({
  selector: 'app-user-list',
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit {

  users: AdminUser[] = [];
  isLoading = true;
  errorMessage = '';
  successMessage = '';

  availableRoles = ['CUSTOMER', 'ADMIN', 'MANAGER'];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.userService.getAll().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load users';
        this.isLoading = false;
      }
    });
  }

  hasRole(user: AdminUser, role: string): boolean {
    return user.roles.includes(role);
  }

  toggleRole(user: AdminUser, role: string): void {
    this.errorMessage = '';
    this.successMessage = '';

    let newRoles: string[];
    if (this.hasRole(user, role)) {
      newRoles = user.roles.filter(r => r !== role);
    } else {
      newRoles = [...user.roles, role];
    }

    if (newRoles.length === 0) {
      this.errorMessage = 'User must have at least one role';
      return;
    }

    this.userService.updateRoles(user.id, newRoles).subscribe({
      next: (updated) => {
        user.roles = updated.roles;
        this.successMessage = `Updated roles for ${user.name}`;
      },
      error: () => {
        this.errorMessage = 'Failed to update roles';
      }
    });
  }

  toggleStatus(user: AdminUser): void {
    const newStatus = user.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE';
    this.userService.updateStatus(user.id, newStatus).subscribe({
      next: (updated) => {
        user.status = updated.status;
      },
      error: () => {
        this.errorMessage = 'Failed to update status';
      }
    });
  }
}
