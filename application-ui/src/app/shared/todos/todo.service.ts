import {Injectable} from '@angular/core';
import {Todo} from './todo';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CreateTodo} from './create-todo';
import {apiPaths} from '../../config';
import {UpdateTodo} from './update-todo';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  constructor(private http: HttpClient) {
  }

  getAllTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(apiPaths.todos, httpOptions);
  }

  getTodoById(id: number): Observable<Todo> {
    return this.http.get<Todo>(apiPaths.todos + `${id}`, httpOptions);
  }

  addTodo(createTodo: CreateTodo): Observable<Todo> {
    return this.http.post<Todo>(apiPaths.todos, createTodo, httpOptions);
  }

  deleteTodoById(id: string): Observable<null> {
    return this.http.delete<null>(apiPaths.todos + `${id}`, httpOptions);
  }

  updateTodo(id: string, updateTodo: UpdateTodo): Observable<Todo> {
    return this.http.put<Todo>(apiPaths.todos + `${id}`, updateTodo, httpOptions);
  }

  toggleTodoComplete(todo: Todo) {
    todo.done = !todo.done;
    return this.updateTodo(todo.id, <UpdateTodo>{
      title: todo.title
    });
  }
}
