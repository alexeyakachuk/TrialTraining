package com.example.TrialTraining.workout.service;

import com.example.TrialTraining.client.model.Client;
import com.example.TrialTraining.client.repository.ClientRepository;
import com.example.TrialTraining.excepion.ConflictTimeException;
import com.example.TrialTraining.excepion.NotFoundException;
import com.example.TrialTraining.trainer.model.Trainer;
import com.example.TrialTraining.trainer.repository.TrainerRepository;
import com.example.TrialTraining.workout.dto.WorkoutDto;
import com.example.TrialTraining.workout.model.Workout;
import com.example.TrialTraining.workout.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.locks.ReentrantLock;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ClientRepository clientRepository,
                              TrainerRepository trainerRepository) {
        this.workoutRepository = workoutRepository;
        this.clientRepository = clientRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public WorkoutDto create(Workout newWorkout) {
        // Проверка соводноли это время
        Client client = clientRepository.findClient(newWorkout.getClientId());
        Trainer trainer = trainerRepository.findTrainer(newWorkout.getTrainerId());
        if (client == null || trainer == null) {
            throw new NotFoundException("Такого тренера или клиента нет");
        }
        lock.lock();
        try {
            if (isCheckTime(newWorkout.getStartTime(), newWorkout.getDate())) {
                throw new ConflictTimeException("На это время уже есть тренировка");
            }
            Workout workout = workoutRepository.create(newWorkout);
//добавить проверки на существование клиента и тренера
            return builderWorkout(workout);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<WorkoutDto> findAllWorkout() {
        return workoutRepository.findAllWorkout().stream()
                .map(workout -> builderWorkout(workout))
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutDto findWorkout(Integer id) {
        Workout workout = workoutRepository.findWorkout(id);
        if (workout == null) {
            throw new NotFoundException("Тренировка с id " + id + " не найдена");
        }
        return builderWorkout(workout);
    }

    @Override
    public void deleteWorkout(Integer id) {
        workoutRepository.deleteWorkout(id);
    }

    private WorkoutDto builderWorkout(Workout workout) {
        return WorkoutDto.builder()
                .clientId(workout.getClientId())
                .trainerId(workout.getTrainerId())
                .date(workout.getDate())
                .startTime(workout.getStartTime())
                .endTime(workout.getEndTime())
                .build();
    }

    private boolean isCheckTime(LocalTime start, LocalDate date) {
        List<Workout> allWorkout = workoutRepository.findAllWorkout();

        for (Workout workout : allWorkout) {
            if (workout.getStartTime().equals(start) && workout.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }
}
