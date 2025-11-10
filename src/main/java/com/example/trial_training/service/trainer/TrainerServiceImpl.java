package com.example.trial_training.service.trainer;

import com.example.trial_training.controller.trainer.CreateTrainerRequest;
import com.example.trial_training.dto.client.ClientDto;
import com.example.trial_training.dto.workout.WorkoutDto;
import com.example.trial_training.exception.NotFoundException;
import com.example.trial_training.model.client.Client;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.repository.trainer.TrainerDbRepository;
import com.example.trial_training.repository.trainer.TrainerRepository;
import com.example.trial_training.dto.trainer.TrainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl {

    private final TrainerDbRepository trainerDbRepository;

    @Autowired
    public TrainerServiceImpl(TrainerDbRepository trainerDbRepository) {
        this.trainerDbRepository = trainerDbRepository;
    }


    public TrainerDto create(Trainer newTrainer) {
        Trainer trainer = trainerDbRepository.create(newTrainer);
        return new TrainerDto(trainer);
    }

    public TrainerDto findTrainer(Integer id) {
        Trainer trainer = trainerDbRepository.findById(id);

        if (trainer == null) {
            throw new NotFoundException("Тренер с id " + id + " не найден");
        }
        return new TrainerDto(trainer);
    }

    public Optional<TrainerDto> findByLogin(String login) {
        Trainer trainer = trainerDbRepository.findByLogin(login);
        return Optional.ofNullable(trainer).map(TrainerDto::new);
    }


    public List<TrainerDto> findAllTrainers() {
        return trainerDbRepository.findByUsers().stream()
                .map(trainer -> new TrainerDto(trainer))
                .toList();
    }


    public void deleteTrainer(Integer id) {
        trainerDbRepository.delete(id);
    }


    public Trainer updateTrainer(Trainer newTrainer) {
        return trainerDbRepository.update(newTrainer);
    }

    public List<WorkoutDto> findAllWorkoutsOfTrainer(Integer id) {
        return trainerDbRepository.findAllWorkoutsOfTrainer(id);
    }


    public List<WorkoutDto> findAllTrainerWorkouts(Integer id) {
        return trainerDbRepository.findAllTrainerWorkouts(id);
    }
}
