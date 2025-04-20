package com.example.TrialTraining.workout.service;

import com.example.TrialTraining.excepion.ConflictTimeException;
import com.example.TrialTraining.excepion.NotFoundException;
import com.example.TrialTraining.workout.dto.WorkoutDto;
import com.example.TrialTraining.workout.model.Workout;
import com.example.TrialTraining.workout.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override
    public WorkoutDto create(Workout newWorkout) {
        // Проверка соводноли это время
        if (isCheckTime(newWorkout.getStartTime())) {
            throw new ConflictTimeException("На это время уже есть тренировка");
        }

        Workout workout = workoutRepository.create(newWorkout);
//добавить проверки на существование клиента и тренера
        return builderWorkout(workout);
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


    private WorkoutDto builderWorkout(Workout workout) {
        return WorkoutDto.builder()
                .clientId(workout.getClientId())
                .trainerId(workout.getTrainerId())
                .startTime(workout.getStartTime())
                .endTime(workout.getEndTime())
                .build();
    }

    private boolean isCheckTime(LocalDateTime start) {
        LocalDateTime end = start.plusHours(1);
        List<Workout> allWorkout = workoutRepository.findAllWorkout();

        for (Workout workout : allWorkout) {
//            if (workout.getStartTime().equals(start)) {
//                return true;
            if (start.isBefore(workout.getEndTime()) && end.isAfter(workout.getStartTime())) {
                return true;
            }
        }
        return false;
    }
}
