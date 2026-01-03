//
//  HomeViewController.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//
//

import Factory
import UIKit
import ComponentKit
import BarVisibilityKit

final class HomeViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: HomePresenterProtocol {
        get { return self.basePresenter as! HomePresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var refreshControl: UIRefreshControl = {
        let refreshControl = UIRefreshControl()
        refreshControl.tintColor = DesignKitColorName.backgroundPrimaryGreen.color
        refreshControl.addTarget(self, action: #selector(didPullToRefresh(_:)), for: .valueChanged)
        refreshControl.translatesAutoresizingMaskIntoConstraints = false
        return refreshControl
    }()
    private lazy var collectionView: UICollectionView = {
        let layout = createCollectionViewLayout()
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.showsVerticalScrollIndicator = false
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.delegate = self
        collectionView.refreshControl = refreshControl
        return collectionView
    }()
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private var dataSource: UICollectionViewDiffableDataSource<HomeSection, HomeItem>!
    
    // MARK: - Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: presenter.homeModeType == .home ? false : true))
        configureNavigationBarVisibility(at: .willAppear(isHidden:  presenter.homeModeType == .home ? true : false))
        checkVisibleCellsForVideoPlayback()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden:  presenter.homeModeType == .home ? false : true))
        Container.shared.deeplinkManager()?.homeDidBecomeReady()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        configureTabBarVisibility(at: .willDisappear)
        configureNavigationBarVisibility(at: .willDisappear)
        stopAllVisibleVideos()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        configureTabBarVisibility(at: .didDisappear)
        stopAllVisibleVideos()
    }
    
    // MARK: - Custom Methods
    @objc private func didPullToRefresh(_ sender: UIRefreshControl) {
        presenter.didPullToRefresh()
    }
    
    @objc private func didTappedBackButton() {
        presenter.didTappedBackButton()
    }
    
    public func triggerPullToRefresh() {
        guard !refreshControl.isRefreshing else { return }

        let offsetPoint = CGPoint(x: 0, y: -collectionView.adjustedContentInset.top - refreshControl.frame.size.height)
        collectionView.setContentOffset(offsetPoint, animated: true)

        DispatchQueue.main.asyncAfter(deadline: .now() + 1) { [weak self] in
            guard let self else { return }
            self.refreshControl.beginRefreshing()
            self.presenter.didPullToRefresh()
        }
    }
}

// MARK: - HomePresenterDelegate
extension HomeViewController: HomePresenterDelegate {
    func prepareUI() {
        view.addSubview(collectionView)
        
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        setupDataSource()
    }
    
    func didApplySnapshot(_ snapshot: NSDiffableDataSourceSnapshot<HomeSection, HomeItem>) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.dataSource.apply(snapshot, animatingDifferences: false)
        }
    }
    
    func didEndRefreshing() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            refreshControl.endRefreshing()
        }
    }
    
    func didChangeLikePost(_ post: Post, at indexPath: IndexPath) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            guard let cell = self.collectionView.cellForItem(at: indexPath) as? PostCollectionViewCell else { return }
            cell.updateLike(post)
        }
    }
    
    func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white
        
        appearance.titleTextAttributes = [
            .foregroundColor: DesignKitColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]
        appearance.shadowColor = .clear
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.leftBarButtonItem = backButton
    }
    
    func scrollToItem(at indexPath: IndexPath) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            collectionView.scrollToItem(at: indexPath, at: .top, animated: false)
        }
    }
}

// MARK: - CollectionView Layout
extension HomeViewController {
    private func createCollectionViewLayout() -> UICollectionViewLayout {
        let layout = UICollectionViewCompositionalLayout { sectionIndex, _ in
            let section = HomeSection.allCases[sectionIndex]
            switch section {
            case .header:
                return self.createHeaderLayout()
            case .stories:
                return self.createStoryLayout()
            case .posts:
                return self.createPostLayout()
            }
        }
        return layout
    }
    
    private func createHeaderLayout() -> NSCollectionLayoutSection {
        let size = NSCollectionLayoutSize(widthDimension: .fractionalWidth(1), heightDimension: .absolute(48))
        let item = NSCollectionLayoutItem(layoutSize: size)
        let group = NSCollectionLayoutGroup.horizontal(layoutSize: size, subitems: [item])
        let section = NSCollectionLayoutSection(group: group)
        section.contentInsets = .init(top: 0, leading: 16, bottom: 0, trailing: 16)
        return section
    }
    
    private func createStoryLayout() -> NSCollectionLayoutSection {
        let size = NSCollectionLayoutSize(widthDimension: .absolute(82), heightDimension: .absolute(105))
        let item = NSCollectionLayoutItem(layoutSize: size)
        let group = NSCollectionLayoutGroup.horizontal(layoutSize: size, subitems: [item])
        let section = NSCollectionLayoutSection(group: group)
        section.orthogonalScrollingBehavior = .continuous
        section.interGroupSpacing = 6
        section.contentInsets = .init(top: 0, leading: 16, bottom: 0, trailing: 16)
        return section
    }
    
    private func createPostLayout() -> NSCollectionLayoutSection {
        let itemSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1),
            heightDimension: .estimated(400)
        )
        
        let item = NSCollectionLayoutItem(layoutSize: itemSize)
        
        let groupSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1),
            heightDimension: .estimated(400)
        )
        
        let group = NSCollectionLayoutGroup.vertical(layoutSize: groupSize, subitems: [item])
        
        let section = NSCollectionLayoutSection(group: group)
        section.interGroupSpacing = 8
        section.contentInsets = NSDirectionalEdgeInsets(top: 32, leading: 0, bottom: 0, trailing: 0)
        
        return section
    }
}

// MARK: - Data Source
extension HomeViewController: UICollectionViewDelegate {
    private func setupDataSource() {
        dataSource = UICollectionViewDiffableDataSource<HomeSection, HomeItem>(collectionView: collectionView) { collectionView, indexPath, model in
            switch model {
            case .header(let model):
                let cell = HomeHeaderCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          appIcon: model.headerIcon,
                          icons: model.icons)
                return cell
            case .story(let model):
                let cell = StoryCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(delegate: self,
                          model: model)
                return cell
            case .post(let model):
                let cell = PostCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(model, delegate: self, indexPath: indexPath)
                return cell
            }
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        presenter.didReachEndOfPosts(indexPath: indexPath)
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        checkVisibleCellsForVideoPlayback()
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        checkVisibleCellsForVideoPlayback()
    }

    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if !decelerate {
            checkVisibleCellsForVideoPlayback()
        }
    }
}

extension HomeViewController {
    private func checkVisibleCellsForVideoPlayback() {
        collectionView.visibleCells.forEach { cell in
            if let postCell = cell as? PostCollectionViewCell {
                if isCellMostlyVisible(postCell) {
                    postCell.handleVideoPlayback(shouldPlay: true)
                } else {
                    postCell.handleVideoPlayback(shouldPlay: false)
                }
            }
        }
    }
    
    private func stopAllVisibleVideos() {
        collectionView.visibleCells.forEach { cell in
            if let postCell = cell as? PostCollectionViewCell {
                postCell.handleVideoPlayback(shouldPlay: false)
            }
        }
    }

    private func isCellMostlyVisible(_ cell: UICollectionViewCell) -> Bool {
        guard let collectionView = cell.superview as? UICollectionView else { return false }
        
        let cellFrameInCollection = collectionView.convert(cell.frame, from: cell.superview)
        let visibleRect = CGRect(origin: collectionView.contentOffset, size: collectionView.bounds.size)
        let intersection = visibleRect.intersection(cellFrameInCollection)
        
        let visibleArea = intersection.width * intersection.height
        let totalArea = cell.frame.width * cell.frame.height
        
        guard totalArea > 0 else { return false }
        
        let visiblePercentage = visibleArea / totalArea
        return visiblePercentage > 0.65
    }
}

// MARK: - StoryCollectionViewCellDelegate
extension HomeViewController: StoryCollectionViewCellDelegate {
    func didTapAddStoryButton() {
        presenter.openAddStory()
    }
    
    func didTapStoryProfile(model: Story) {
        presenter.didTapStoryProfile(model)
    }
}

// MARK: - PostCollectionViewCellDelegate
extension HomeViewController: PostCollectionViewCellDelegate {
    func didTappedUsernameInHeaderView(_ username: String, userId: String) {
        presenter.openProfile(username: username, userId: userId)
    }
    
    func didTappedTripleButtonInHeaderView(_ model: Post) {
        presenter.openPostSetting(model)
    }
    
    func didTappedActionButton(tag: Int, indexPath: IndexPath) {
        presenter.didTappedActionButton(tag: tag, indexPath: indexPath)
    }
    
    func didTappedUsernameInLikeView(_ username: String, userId: String) {
        presenter.openProfile(username: username, userId: userId)
    }
    
    func didTappedUsernameInCommentView(_ username: String, userId: String) {
        presenter.openProfile(username: username, userId: userId)
    }
    
    func didZoomingImagePost(started: Bool) {
        collectionView.isScrollEnabled = !started
    }
}

extension HomeViewController: HomeHeaderCollectionViewCellDelegate {
    func didTappedHomeHeaderIcon(_ tag: Int) {
        presenter.didTappedHomeHeaderIcon(tag)
    }
}
