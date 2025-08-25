package com.example.trial_training.service.trainer;

import com.example.trial_training.controller.trainer.CreateTrainerRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.NotFoundException;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.repository.trainer.TrainerRepository;
import com.example.trial_training.dto.trainer.TrainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public TrainerDto create(CreateTrainerRequest newTrainer) {
        Trainer trainer = trainerRepository.create(newTrainer);
        return new TrainerDto(trainer);
    }
    @Override
    public Optional<TrainerDto> findByLogin(String login) {
        Trainer trainer = trainerRepository.findTrainerByLogin(login);
        return Optional.ofNullable(trainer).map(TrainerDto::new);
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
    public TrainerDto findTrainer(Integer id) {
        Trainer trainer = trainerRepository.findTrainer(id);

        if (trainer == null) {
            throw new NotFoundException("Тренер с id " + id + " не найден");
        }
        return new TrainerDto(trainer);
    }

    @Override
    public List<WorkoutDto> findAllWorkoutsOfTrainer(Integer id) {
        return trainerRepository.findAllWorkoutsOfTrainer(id);
    }

    @Override
    public List<WorkoutDto> findAllTrainerWorkouts(Integer id) {
        return trainerRepository.findAllTrainerWorkouts(id);
    }
}
