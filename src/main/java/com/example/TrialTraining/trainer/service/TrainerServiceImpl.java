package com.example.TrialTraining.trainer.service;

import com.example.TrialTraining.excepion.ConflictException;
import com.example.TrialTraining.excepion.NotFoundException;
import com.example.TrialTraining.trainer.model.Trainer;
import com.example.TrialTraining.trainer.repository.TrainerRepository;
import com.example.TrialTraining.trainer.trainerDto.TrainerDto;
import com.example.TrialTraining.trainingCalendar.model.TrainingCalendar;
import com.example.TrialTraining.trainingCalendar.repository.TrainerCalendarRepository;
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
    public TrainerDto create(Trainer newTrainer) {
        Trainer trainer = trainerRepository.create(newTrainer);
        return builderTrainer(trainer);
    }

    @Override
    public List<TrainerDto> findAllTrainer() {
        List<Trainer> trainers = trainerRepository.findAllTrainer();
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
