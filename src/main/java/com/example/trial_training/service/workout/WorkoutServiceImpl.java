package com.example.trial_training.service.workout;

import com.example.trial_training.controller.workout.CreateWorkoutRequest;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.repository.client.ClientDbRepository;
import com.example.trial_training.repository.client.ClientRepository;
import com.example.trial_training.exception.ConflictTimeException;
import com.example.trial_training.exception.NotFoundException;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.repository.trainer.TrainerDbRepository;
import com.example.trial_training.repository.trainer.TrainerRepository;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.model.workout.Workout;
import com.example.trial_training.repository.workout.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ClientDbRepository clientDbRepository;
    private final TrainerDbRepository trainerDbRepository;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ClientDbRepository clientDbRepository,
                              TrainerDbRepository trainerDbRepository) {
        this.workoutRepository = workoutRepository;
        this.clientDbRepository = clientDbRepository;
        this.trainerDbRepository = trainerDbRepository;
    }

    @Override
    public WorkoutDto create(CreateWorkoutRequest newWorkout) {
        // Проверка соводноли это время
        Client client = clientDbRepository.findById(newWorkout.getClientId());
        Trainer trainer = trainerDbRepository.findById(newWorkout.getTrainerId());
        if (client == null || trainer == null) {
            throw new NotFoundException("Такого тренера или клиента нет");
        }

        try {

            Workout workout = workoutRepository.create(newWorkout);
            return new WorkoutDto(workout, client, trainer);

        } catch (RuntimeException e) {
            throw new ConflictTimeException("Это время на тренировку занято");

        }
    }

    @Override
    //Переделать. Возврощать просто Workout
    public List<WorkoutDto> findAllWorkouts() {
        return workoutRepository.findAllWorkouts().stream()
                .map(workout -> {
                    Integer clientId = workout.getClientId();
                    Client client = clientDbRepository.findById(clientId);
                    Integer trainerId = workout.getTrainerId();
                    Trainer trainer = trainerDbRepository.findById(trainerId);
                    return new WorkoutDto(workout, client, trainer);
                })
                .toList();
    }
//Пока не используется
    @Override
    public WorkoutDto findWorkout(Integer id) {
        Workout workout = workoutRepository.findWorkout(id);
        if (workout == null) {
            throw new NotFoundException("Тренировка с id " + id + " не найдена");
        }
        Integer clientId = workout.getClientId();
        Client client = clientDbRepository.findById(clientId);
        Integer trainerId = workout.getTrainerId();
        Trainer trainer = trainerDbRepository.findById(trainerId);
        return new WorkoutDto(workout, client, trainer);
    }

    @Override
    public void deleteWorkout(Integer id) {
        workoutRepository.deleteWorkout(id);
    }

}
