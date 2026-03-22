package rozchepiy.dev.logisticsaggregator.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rozchepiy.dev.logisticsaggregator.dto.LoaderDTO;
import rozchepiy.dev.logisticsaggregator.exception.AlreadyExistException;
import rozchepiy.dev.logisticsaggregator.exception.NotFoundException;
import rozchepiy.dev.logisticsaggregator.model.LoaderProfile;
import rozchepiy.dev.logisticsaggregator.model.User;
import rozchepiy.dev.logisticsaggregator.model.enums.Role;
import rozchepiy.dev.logisticsaggregator.repository.LoaderProfileRepository;
import rozchepiy.dev.logisticsaggregator.repository.UserRepository;
import rozchepiy.dev.logisticsaggregator.service.LoaderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoaderServiceImpl implements LoaderService {

    private final LoaderProfileRepository loaderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<LoaderDTO> getAllLoaders() {
        List<LoaderProfile> loaders = loaderRepository.findAll();
        return loaders.stream()
                .map(loader -> modelMapper.map(loader, LoaderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public LoaderDTO getLoaderById(Long id) {
        LoaderProfile loader = loaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loader profile not found with id: " + id));
        return modelMapper.map(loader, LoaderDTO.class);
    }

    @Override
    @Transactional
    public LoaderDTO createLoaderProfile(Long userId, LoaderDTO loaderDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (user.getLoaderProfile() != null) {
            throw new AlreadyExistException("User already has a loader profile");
        }

        user.getRoles().add(Role.LOADER);

        LoaderProfile loaderProfile = modelMapper.map(loaderDTO, LoaderProfile.class);
        loaderProfile.setUser(user);
        loaderProfile.setLoaderRating(5.0);

        LoaderProfile savedProfile = loaderRepository.save(loaderProfile);
        userRepository.save(user);

        return modelMapper.map(savedProfile, LoaderDTO.class);
    }

    @Override
    @Transactional
    public LoaderDTO updateLoaderProfile(Long id, LoaderDTO loaderDTO) {
        LoaderProfile existingProfile = loaderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loader profile not found with id: " + id));

        existingProfile.setAge(loaderDTO.getAge());
        existingProfile.setHeight(loaderDTO.getHeight());
        existingProfile.setWeight(loaderDTO.getWeight());

        LoaderProfile updatedProfile = loaderRepository.save(existingProfile);
        return modelMapper.map(updatedProfile, LoaderDTO.class);
    }

    @Override
    @Transactional
    public void deleteLoaderProfile(Long id) {
        LoaderProfile profile = loaderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loader profile not found with id: " + id));

        User user = profile.getUser();
        user.getRoles().remove(Role.LOADER);
        userRepository.save(user);

        loaderRepository.delete(profile);
    }
}
