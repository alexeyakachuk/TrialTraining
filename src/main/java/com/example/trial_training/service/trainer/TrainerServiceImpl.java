package com.example.trial_training.service.trainer;

import com.example.trial_training.controller.trainer.CreateTrainerRequest;
import com.example.trial_training.dto.AllWorkoutDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.NotFoundException;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.repository.trainer.TrainerRepository;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.service.workout.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final WorkoutService workoutService;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository,
                              WorkoutService workoutService) {
        this.trainerRepository = trainerRepository;
        this.workoutService = workoutService;
    }

    @Override
    public TrainerDto create(CreateTrainerRequest newTrainer) {
        Trainer trainer = trainerRepository.create(newTrainer);
        return new TrainerDto(trainer);
    }

    @Override
    public List<TrainerDto> findAllTrainers() {
        return trainerRepository.findAllTrainers().stream()
                .map(trainer -> new TrainerDto(trainer))
                .toList();
    }

    @Override
    public void deleteTrainer(Integer id) {
        trainerRepository.deleteTrainer(id);
    }

    @Override
    public Integer updateTrainer(Trainer newTrainer) {
        return trainerRepository.updateTrainer(newTrainer);
    }

    @Override
    public List<WorkoutDto> findAllTrainerWorkouts(Integer trainerId) {
        return workoutService.findAllWorkouts().stream()
                .filter(workoutDto -> workoutDto.getTrainer().getId().equals(trainerId))
                .toList();
    }

    @Override
    public TrainerDto findTrainer(Integer id) {
        Trainer trainer = trainerRepository.findTrainer(id);

        if (trainer == null) {
            throw new NotFoundException("Тренер с id " + id + " не найден");
        }
        return new TrainerDto(trainer);
    }

    @Override
    public List<AllWorkoutDto> findAllWorkoutsOfTrainer(Integer id) {
        return trainerRepository.findAllWorkoutsOfTrainer(id);
    }
}
