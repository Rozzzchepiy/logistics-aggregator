package rozchepiy.dev.logisticsaggregator.service;

import rozchepiy.dev.logisticsaggregator.dto.LoaderDTO;

import java.util.List;

public interface LoaderService {
    List<LoaderDTO> getAllLoaders();
    LoaderDTO getLoaderById(Long id);
    LoaderDTO createLoaderProfile(Long userId, LoaderDTO loaderDTO);
    LoaderDTO updateLoaderProfile(Long id, LoaderDTO loaderDTO);
    void deleteLoaderProfile(Long id);
}
