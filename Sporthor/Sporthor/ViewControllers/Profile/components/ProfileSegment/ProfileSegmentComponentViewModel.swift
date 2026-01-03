//
//  ProfileSegmentComponentViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import ComponentBaseKit
import UIKit

public protocol ProfileSegmentViewModelDelegate: AnyObject {
    func contentSizeChanged()
    func showSegmentLoading()
    func hideSegmentLoading()
}

enum ViewState {
    case initial, loading, loaded, error(Error)
}

final class ProfileSegmentComponentViewModel: CollectionComponentViewModel {
    typealias CellType = PostImagesCell
    let data: ProfileSegmentComponent.Data
    var defaultInsets: UIEdgeInsets
    private weak var delegate: ProfileSegmentViewModelDelegate?
    
    private var sectionHeaderViewModel: ProfileSectionHeaderViewModel?
    private var selectedSegment: SegmentItem?
    private var viewModels: [SegmentType: any BaseSegmentViewModel] = [:]
    private var currentTask: Task<Void, Never>?
    private var loadedSegments: Set<SegmentType> = []

    @MainActor private(set) var state: ViewState = .initial {
        didSet { handleStateChange() }
    }
    
    // MARK: - Initializer
    
    init(data: ProfileSegmentComponent.Data, defaultInsets: UIEdgeInsets, delegate: ProfileSegmentViewModelDelegate?) {
        self.data = data
        self.defaultInsets = defaultInsets
        self.delegate = delegate
        setupViewModels()
        
        if let firstSegment = data.segments.first {
            switchToSegment(firstSegment)
        }
    }
    
    var segments: [SegmentItem] { data.segments }
    var selectedIndex: Int { data.selectedSegmentIndex }
    
    var insets: UIEdgeInsets {
        UIEdgeInsets(top: 6, left: .zero, bottom: 6, right: .zero)
    }
    
    var lineSpacing: CGFloat {
        3
    }
    
    var interitemSpacing: CGFloat {
        3
    }
    
    private func setupViewModels() {
        viewModels = [
            .posts: PostsViewModel(),
            .matches: MatchesViewModel(),
            .teamSquad: TeamSquadViewModel(),
            .personalInfo: PersonalInfoViewModel()
        ]
        sectionHeaderViewModel = ProfileSectionHeaderViewModel(tabs: segments, selectedTabIndex: selectedIndex)
    }
    
    func updateViewModels() {
        loadedSegments.removeAll()
        if let firstSegment = data.segments.first {
            switchToSegment(firstSegment)
        }
    }

    func switchToSegment(_ segment: SegmentItem) {
        selectedSegment = segment
        if let index = segments.firstIndex(where: { $0.type == segment.type }) {
            sectionHeaderViewModel?.selectedTabIndex = index
        }
        Task { await fetchTabData(selectedTab: segment) }
    }
    
    func getViewModel(for type: SegmentType) -> (any BaseSegmentViewModel)? {
        return viewModels[type]
    }
    
    var numberOfItems: Int {
        return getViewModel(for: selectedSegment?.type ?? .posts)?.numberOfItems ?? .zero
    }
    
    // MARK: - CollectionView Methods
    
    func cell(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionViewCell {
        return getViewModel(
            for: selectedSegment?.type ?? .posts
        )?.cell(in: collectionView, at: indexPath, delegate: delegate) ?? collectionView.dequeueEmptyReusableCell(with: indexPath)
    }
    
    func size(_ collectionView: UICollectionView, at indexPath: IndexPath) -> CGSize {
        return getViewModel(
            for: selectedSegment?.type ?? .posts
        )?.size(collectionView, at: indexPath) ?? .zero
    }
    
    func sectionHeader(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionReusableView {
        guard let header = collectionView.dequeueReusableSupplementaryView(
            ofKind: UICollectionView.elementKindSectionHeader,
            withReuseIdentifier: ProfileSectionHeaderView.reuseIdentifier,
            for: indexPath
        ) as? ProfileSectionHeaderView,
        let sectionHeaderViewModel
        else { return collectionView.dequeueEmptyHeaderReusableView(with: indexPath) }
        header.configure(delegate: self, viewModel: sectionHeaderViewModel)
        return header
    }
    
    func sectionHeaderSize(_ collectionView: UICollectionView, at section: Int) -> CGSize {
        return CGSize(width: collectionView.frame.size.width, height: 52)
    }

    // MARK: - Data Fetching
    
    private func fetchDataForTab(type: SegmentType) async throws -> Any? {
        guard let viewModel = getViewModel(for: type) else {
            throw ViewModelError.viewModelNotFound
        }
        return await viewModel.fetch(userId: data.userId)
    }
    
    private func fetchTabData(selectedTab: SegmentItem) async {
        guard !loadedSegments.contains(selectedTab.type) else {
            await MainActor.run { self.state = .loaded }
            return
        }
        
        await MainActor.run { self.state = .loading }
        currentTask?.cancel()
        
        currentTask = Task { @MainActor in
            do {
                if let viewModel = getViewModel(for: selectedTab.type) {
                    let result = try await fetchDataForTab(type: selectedTab.type)
                    viewModel.updateData(with: result)
                }
                
                loadedSegments.insert(selectedTab.type)
                if !Task.isCancelled { self.state = .loaded }
            } catch {
                if !Task.isCancelled { self.state = .error(error) }
            }
        }
    }
    
    // MARK: - UI Updates
    
    @MainActor
    private func handleStateChange() {
        switch state {
        case .loading, .initial:
            showLoading()
        case .loaded:
            hideLoading()
            delegate?.contentSizeChanged()
        case .error(let error):
            hideLoading()
            print("Error: \(error.localizedDescription)")
        }
    }
    
    @MainActor
    private func showLoading() {
        delegate?.hideSegmentLoading()
        delegate?.showSegmentLoading()
    }
    
    @MainActor
    private func hideLoading() {
        delegate?.hideSegmentLoading()
    }
}

extension ProfileSegmentComponentViewModel: SectionHeaderDelegate {
    func didSelectTab(at item: SegmentItem) {
        switchToSegment(item)
    }
}

// MARK: - Error Types

private enum ViewModelError: Error {
    case viewModelNotFound
}
