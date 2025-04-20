import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Movie } from '../../models/movie.model';
import { MovieService } from '../../services/movie.service';
import { AuthService } from '../../services/auth.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  selectedMovies: Movie[] = [];
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
  constructor(
    private authService: AuthService,
    private router: Router,
    private movieService: MovieService,
    private snackBar: MatSnackBar
  ) {}

  onMoviesSelected(movies: Movie[]): void {
    this.selectedMovies = movies;
  }

  addToDatabase(movies: Movie[]): void {
    this.movieService.batchAddMovies(movies).subscribe({
      next: () => {
        this.snackBar.open('Movies added successfully', 'Close', { duration: 3000 });
        this.selectedMovies = [];
      },
      error: (error) => {
        this.snackBar.open(
          error.error?.message || 'Failed to add movies',
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  deleteFromDatabase(movieIds: number[]): void {
    this.movieService.batchDeleteMovies(movieIds).subscribe({
      next: () => {
        this.snackBar.open('Movies deleted successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        this.snackBar.open(
          error.error?.message || 'Failed to delete movies',
          'Close',
          { duration: 3000 }
        );
      }
    });

  }

}
