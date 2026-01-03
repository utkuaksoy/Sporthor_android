//
//  FollowersViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import AutoLayout
import UIKit
import CommonKit
import ComponentBaseKit
import BarVisibilityKit
import DesignKit

final class FollowersViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {

    // MARK: - VIPER Variables
    var presenter: FollowersPresenterProtocol {
        get { return self.basePresenter as! FollowersPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements

    private lazy var tabCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumLineSpacing = .zero
        layout.minimumInteritemSpacing = .zero
        
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .white
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.register(FollowTabCollectionViewCell.self, forCellWithReuseIdentifier: FollowTabCollectionViewCell.reuseIdentifier)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    private lazy var indicatorView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.borderSoft200.color
        return view
    }()
    
    private lazy var contentCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumLineSpacing = 0
        layout.minimumInteritemSpacing = 0
        
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.backgroundColor = .clear
        collectionView.isPagingEnabled = true
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.bounces = false
        collectionView.register(FollowContentCollectionViewCell.self, forCellWithReuseIdentifier: FollowContentCollectionViewCell.reuseIdentifier)
        collectionView.translatesAutoresizingMaskIntoConstraints = false
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
    
    // MARK: - Private Properties

    private var tabCounts: [Int] = [0, 0, 0]
    private var tabs: [(title: String, count: Int)] = []
    
    private var selectedTabIndex: Int = 0 {
        didSet {
            if oldValue != selectedTabIndex {
                DispatchQueue.main.async { [weak self] in
                    guard let self = self else { return }
                    self.tabCollectionView.selectItem(
                        at: IndexPath(item: self.selectedTabIndex, section: .zero),
                        animated: true,
                        scrollPosition: .centeredHorizontally
                    )
                    self.contentCollectionView.scrollToItem(
                        at: IndexPath(item: self.selectedTabIndex, section: .zero),
                        at: .centeredHorizontally,
                        animated: true
                    )
                    self.presenter.setCurrentTab(FollowDirectionEnum.allCases[self.selectedTabIndex])
                }
            }
        }
    }
    
    // MARK: - Lifecycles
    
    deinit {
        removeNotificationObservers()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        selectedTabIndex = FollowDirectionEnum.allCases.firstIndex(of: presenter.currentTab) ?? .zero
        presenter.viewDidLoad()
        setupNotificationObservers()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
        presenter.viewWillAppear()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: false))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        configureTabBarVisibility(at: .willDisappear)
        configureNavigationBarVisibility(at: .willDisappear)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        configureTabBarVisibility(at: .didDisappear)
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()

        tabCollectionView.selectItem(
            at: IndexPath(item: selectedTabIndex, section: .zero),
            animated: false,
            scrollPosition: .left
        )

        contentCollectionView.scrollToItem(
            at: IndexPath(item: selectedTabIndex, section: .zero),
            at: .centeredHorizontally,
            animated: false
        )
    }
    
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
    
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white
        
        appearance.titleTextAttributes = [
            .foregroundColor: ColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]
        appearance.shadowColor = .clear
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.title = presenter.userName
        navigationItem.leftBarButtonItem = backButton
    }
    
    private func configureTabs() {
        tabs.append(("Takipçi", tabCounts[0]))
        tabs.append(("Takip", tabCounts[1]))
        if !presenter.isCurrentUser {
            tabs.append(("Ortak", tabCounts[2]))
        }
    }
    
    private func setupNotificationObservers() {
        FollowingManager.shared.addObserver(self, selector: #selector(handleFollowingStatusChange(_:)))
    }
    
    private func removeNotificationObservers() {
        FollowingManager.shared.removeObserver(self)
    }
    
    @objc
    private func handleFollowingStatusChange(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let userId = userInfo["userId"] as? String,
              let isFollowing = userInfo["isFollowing"] as? Bool else {
            return
        }
        presenter.handleFollowingStatusChange(userId: userId, isFollowing: isFollowing)
    }
}

// MARK: - Setup

private extension FollowersViewController {
    func setupViews() {
        view.addSubview(tabCollectionView) {
            $0.pin(edges: [.leading, .trailing, .top], to: view)
            $0.height == 40
        }
        view.addSubview(indicatorView) {
            $0.pin(edges: [.leading, .trailing], to: view)
            $0.top == tabCollectionView.bottomAnchor
            $0.height == 1
        }
        view.addSubview(contentCollectionView) {
            $0.pin(edges: [.leading, .trailing, .bottom], to: view)
            $0.top == indicatorView.bottomAnchor
        }
    }
}

// MARK: - UICollectionViewDataSource & UICollectionViewDelegateFlowLayout

extension FollowersViewController: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return tabs.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == tabCollectionView {
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FollowTabCollectionViewCell.reuseIdentifier, for: indexPath) as? FollowTabCollectionViewCell else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
            let tab = tabs[indexPath.item]
            cell.configure(title: tab.title, count: tab.count)
            cell.isSelected = indexPath.item == selectedTabIndex
            return cell
        } else {
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FollowContentCollectionViewCell.reuseIdentifier, for: indexPath) as? FollowContentCollectionViewCell else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
            cell.delegate = self
            cell.reloadData()
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == tabCollectionView {
            return CGSize(width: collectionView.bounds.width / CGFloat(tabs.count), height: collectionView.bounds.height)
        } else {
            return collectionView.bounds.size
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == tabCollectionView {
            DispatchQueue.main.async { [weak self] in
                self?.selectedTabIndex = indexPath.row
            }
        }
    }
}

// MARK: - UIScrollViewDelegate

extension FollowersViewController: UIScrollViewDelegate {
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if scrollView == contentCollectionView {
            let page = Int(scrollView.contentOffset.x / scrollView.bounds.width)
            DispatchQueue.main.async { [weak self] in
                self?.selectedTabIndex = page
            }
        }
    }
}

// MARK: - FollowContentCollectionViewCellDelegate

extension FollowersViewController: FollowContentCollectionViewCellDelegate {
    func followButtonTapped(at indexPath: IndexPath) {
        presenter.followButtonTapped(at: indexPath)
    }
    
    func numberOfRows() -> Int {
        return presenter.numberOfRows()
    }
    
    func cellForRow(at indexPath: IndexPath) -> FollowerModel? {
        return presenter.cellForRow(at: indexPath)
    }
    
    func didSelectRow(at indexPath: IndexPath) {
        presenter.didSelectRow(at: indexPath)
    }
}

// MARK: - FollowersPresenterDelegate

extension FollowersViewController: FollowersPresenterDelegate {
    func configureView() {
        DispatchQueue.main.async {
            self.configureNavigationBar()
        }
    }
    
    func reloadData() {
        DispatchQueue.main.async {
            self.contentCollectionView.reloadData()
        }
    }
    
    func reloadRow(index: Int) {
        let indexPath = IndexPath(row: index, section: .zero)
        DispatchQueue.main.async { [weak self] in
            self?.contentCollectionView.performBatchUpdates {
                self?.contentCollectionView.reloadItems(at: [indexPath])
            }
        }
    }
    
    func updateTabCounts(followers: Int, following: Int, mutual: Int) {
        tabCounts = [followers, following, mutual]
        configureTabs()
        DispatchQueue.main.async {
            self.tabCollectionView.reloadData()
        }
    }
    
    func showLoading() {
        DispatchQueue.main.async {
            BaseHelper.shared.showIndicator()
        }
    }
    
    func hideLoading() {
        DispatchQueue.main.async {
            BaseHelper.shared.hideIndicator()
        }
    }
    
    func showError(_ error: Error) {
        DispatchQueue.main.async {
            // Show error alert
        }
    }
}

