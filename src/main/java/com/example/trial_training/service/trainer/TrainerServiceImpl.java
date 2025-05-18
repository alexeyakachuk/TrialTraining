package com.example.trial_training.service.trainer;

import com.example.trial_training.CreateTrainerRequest;
import com.example.trial_training.exception.NotFoundException;
import com.example.trial_training.model.trainer.Trainer;
import com.example.trial_training.repository.trainer.TrainerRepository;
import com.example.trial_training.dto.trainer.TrainerDto;
import com.example.trial_training.trainingCalendar.model.TrainingCalendar;
import com.example.trial_training.trainingCalendar.repository.TrainerCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerCalendarRepository trainerCalendarRepository;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository, TrainerCalendarRepository trainerCalendarRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerCalendarRepository = trainerCalendarRepository;
    }

    @Override
    public TrainerDto create(CreateTrainerRequest newTrainer) {
        Trainer trainer = trainerRepository.create(newTrainer);
        return builderTrainer(trainer);
    }

    @Override
    public List<TrainerDto> findAllTrainers() {
        List<Trainer> trainers = trainerRepository.findAllTrainers();
        List<TrainerDto> allTrainers = new ArrayList<>();

        for (Trainer trainer : trainers) {
            TrainerDto trainerDto = builderTrainer(trainer);
            allTrainers.add(trainerDto);
        }

        return allTrainers;
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
    public List<TrainingCalendar> findAllTrainerWorkouts(Integer id) {
        return trainerCalendarRepository.findAllTrainerWorkouts(id);
    }

    @Override
    public TrainerDto findTrainer(Integer id) {
        Trainer trainer = trainerRepository.findTrainer(id);

        if (trainer == null) {
            throw new NotFoundException("Тренер с id " + id + " не найден");
        }
        return builderTrainer(trainer);
    }

    private TrainerDto builderTrainer(Trainer trainer) {
        return TrainerDto.builder()
                .name(trainer.getName())
                .surname(trainer.getSurname())
                .birthday(trainer.getBirthday())
                .telephone(trainer.getTelephone())
                .email(trainer.getEmail())
                .build();
    }
}
