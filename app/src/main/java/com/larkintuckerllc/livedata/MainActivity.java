package com.larkintuckerllc.livedata;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodosAdapter mTodosAdapter;
    private TodosViewModel mTodosViewModel;
    private List<Todo> mTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView todosRecyclerView = findViewById(R.id.rvTodos);
        mTodosViewModel = ViewModelProviders.of(this).get(TodosViewModel.class);
        final Observer<List<Todo>> todosObserver = new Observer<List<Todo>>() {
            @Override
            public void onChanged(@Nullable final List<Todo> todos) {
                if (mTodos == null) {
                   mTodos = todos;
                   mTodosAdapter = new TodosAdapter();
                   todosRecyclerView.setAdapter(mTodosAdapter);
               } else {
                   DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {

                       @Override
                       public int getOldListSize() {
                                                         return mTodos.size();
                                                                              }

                       @Override
                       public int getNewListSize() {
                                                         return todos.size();
                                                                             }

                       @Override
                       public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                           return mTodos.get(oldItemPosition).getId() ==
                               todos.get(newItemPosition).getId();
                       }

                       @Override
                       public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                           Todo oldTodo = mTodos.get(oldItemPosition);
                           Todo newTodo = todos.get(newItemPosition);
                           return oldTodo.equals(newTodo);
                   }

               });
               result.dispatchUpdatesTo(mTodosAdapter);
               mTodos = todos;
           }
            }
        };
        mTodosViewModel.getTodos().observe(this, todosObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                final EditText nameEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new todo")
                        .setMessage("What do you want to do next?")
                        .setView(nameEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name= String.valueOf(nameEditText.getText());
                                long date = (new Date()).getTime();
                                mTodosViewModel.addTodo(name, date);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {

        @Override
        public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
            return new TodoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TodoViewHolder holder, int position) {
            Todo todo = mTodos.get(position);
            holder.getNameTextView().setText(todo.getName());
            holder.getDateTextView().setText((new Date(todo.getDate()).toString()));
        }

        @Override
        public int getItemCount() {
            return mTodos.size();
        }

        class TodoViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTvName;
            private final TextView mTvDate;

            TodoViewHolder(View itemView) {
                super(itemView);
                mTvName = itemView.findViewById(R.id.tvName);
                mTvDate = itemView.findViewById(R.id.tvDate);
                Button btnDelete = itemView.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        Todo todo = mTodos.get(pos);
                        mTodosViewModel.removeTodo(todo.getId());
                    }
                });
            }

            TextView getNameTextView() {
                return mTvName;
            }

            TextView getDateTextView() {
                return mTvDate;
            }

        }
    }

}
