//
//  Homev2ViewController.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//
//

import UIKit
import ComponentKit
import BarVisibilityKit
import Factory

extension Notification.Name {
    static let addPost = Notification.Name("addPost")
}

final class Homev2ViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
    // MARK: - VIPER Variables
    var presenter: Homev2PresenterProtocol {
        get { return self.basePresenter as! Homev2PresenterProtocol }
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
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.refreshControl = refreshControl
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.keyboardDismissMode = .onDrag
        tableView.separatorStyle = .none
        tableView.allowsSelection = false
        tableView.allowsSelectionDuringEditing = false
        tableView.showsVerticalScrollIndicator = false
        tableView.showsHorizontalScrollIndicator = false
        return tableView
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
    
    // MARK: - Members
    
    // MARK: - Lifecycles
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

        let offsetPoint = CGPoint(x: 0, y: -tableView.adjustedContentInset.top - refreshControl.frame.size.height)
        tableView.setContentOffset(offsetPoint, animated: true)

        DispatchQueue.main.asyncAfter(deadline: .now() + 1) { [weak self] in
            guard let self else { return }
            self.refreshControl.beginRefreshing()
            self.presenter.didPullToRefresh()
        }
    }
    
    @objc private func handleAddPost() {
        triggerPullToRefresh()
    }
}

// MARK: - Homev2PresenterDelegate
extension Homev2ViewController: Homev2PresenterDelegate {
    func prepareUI() {
        NotificationCenter.default.addObserver(self, selector: #selector(handleAddPost), name: .addPost, object: nil)
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
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
            guard let cell = self.tableView.cellForRow(at: indexPath) as? PostTableViewCell else { return }
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
            tableView.scrollToRow(at: indexPath, at: .middle, animated: false)
        }
    }
}

// MARK: - UITableViewDataSource
extension Homev2ViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return presenter.homeModeType == .home ? 1 : 0
        case 1:
            return presenter.homeModeType == .home ? 1 : 0
        case 2:
            return presenter.posts.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = HomeHeaderTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      appIcon: Icons.headerApp,
                      icons: [Icons.bellBadge,
                              Icons.calendarBadge])
            return cell
        case 1:
            let cell = StoriesTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self, stories: presenter.stories)
            return cell
        case 2:
            let cell = PostTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(presenter.posts[indexPath.row], delegate: self, indexPath: indexPath)
            return cell
        default:
            return UITableViewCell()
        }
    }
}
// MARK: - UITableViewDelegate
extension Homev2ViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return presenter.homeModeType == .home ? 24 : 0
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
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

extension Homev2ViewController {
    private func checkVisibleCellsForVideoPlayback() {
        tableView.visibleCells.forEach { cell in
            if let postCell = cell as? PostTableViewCell {
                if isCellMostlyVisible(postCell) {
                    postCell.handleVideoPlayback(shouldPlay: true)
                } else {
                    postCell.handleVideoPlayback(shouldPlay: false)
                }
            }
        }
    }
    
    private func stopAllVisibleVideos() {
        tableView.visibleCells.forEach { cell in
            if let postCell = cell as? PostTableViewCell {
                postCell.handleVideoPlayback(shouldPlay: false)
            }
        }
    }

    private func isCellMostlyVisible(_ cell: UITableViewCell) -> Bool {
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
extension Homev2ViewController: StoryCollectionViewCellDelegate {
    func didTapAddStoryButton() {
        presenter.openAddStory()
    }
    
    func didTapStoryProfile(model: Story) {
        presenter.didTapStoryProfile(model)
    }
}

// MARK: - PostTableViewCellDelegate
extension Homev2ViewController: PostTableViewCellDelegate {
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
        tableView.isScrollEnabled = !started
    }
}

// MARK: - HomeHeaderTableViewCellDelegate
extension Homev2ViewController: HomeHeaderTableViewCellDelegate {
    func didTappedHomeHeaderIcon(_ tag: Int) {
        presenter.didTappedHomeHeaderIcon(tag)
    }
}
