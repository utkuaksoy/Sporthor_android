//
//  GroupDetailViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import DesignKit
import UIKit

final class GroupDetailViewController: BaseViewController {
    
    // MARK: - Section Types
    
    private enum HeaderItem {
        case groupInfo(imageUrl: String, name: String, groupUserCount: Int)
    }
    
    private enum FooterItem {
        case media(count: Int)
        case leaveGroup
    }

    // MARK: - VIPER Variables

    var presenter: GroupDetailPresenterProtocol {
        get { return self.basePresenter as! GroupDetailPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - Private UI Elements
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = createLayout()
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .white
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.showsVerticalScrollIndicator = false
        
        // Register cells
        collectionView.register(GroupHeaderCell.self, forCellWithReuseIdentifier: GroupHeaderCell.reuseIdentifier)
        collectionView.register(FollowerCollectionViewCell.self, forCellWithReuseIdentifier: FollowerCollectionViewCell.reuseIdentifier)
        collectionView.register(GroupFooterCell.self, forCellWithReuseIdentifier: GroupFooterCell.reuseIdentifier)
        
        // Register supplementary views
        collectionView.register(
            GroupMembersHeaderView.self,
            forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader,
            withReuseIdentifier: GroupMembersHeaderView.reuseIdentifier
        )
        
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    // MARK: - Private Properties
    private var headerHeightConstraint: NSLayoutConstraint!
    private var headerItem: HeaderItem?
    private var footerItems: [FooterItem] = []
    
    // MARK: - Lifecycles
    
    deinit {
        removeNotificationObservers()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        setupConstraints()
        configureNavigationBar()
        presenter.viewDidLoad()
        setupNotificationObservers()
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
              let followStatus = userInfo["followStatus"] as? ProfileActionButtonType else {
            return
        }
        presenter.handleFollowingStatusChange(userId: userId, isFollowing: followStatus == .following)
    }
    
    // MARK: - Actions
    
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
    
    @objc
    private func didTapMedia() {
        presenter.didTapMediaButton()
    }
    
    @objc
    private func didTapLeaveGroup() {
        presenter.didTapLeaveGroup()
    }
}

// MARK: - GroupDetailPresenterDelegate

extension GroupDetailViewController: GroupDetailPresenterDelegate {
    func configureViews() {
        // Header configuration will be handled by presenter
    }
    
    func reloadData() {
        collectionView.reloadData()
    }
    
    func removeRow(for indexPath: IndexPath) {
        collectionView.performBatchUpdates {
            collectionView.deleteItems(at: [indexPath])
        }
    }
    
    func updateFollowStatus(at indexPath: IndexPath, isFollowing: Bool) {
        DispatchQueue.main.async { [weak self] in
            self?.collectionView.reloadItems(at: [indexPath])
        }
    }
    
    func configureGroupDetail() {
        guard let detail = presenter.groupDetailResponse else { return }
        
        headerItem = .groupInfo(
            imageUrl: detail.groupImageUrl,
            name: detail.groupName,
            groupUserCount: detail.members.count
        )
        
        footerItems = [
            .media(count: detail.mediaCount),
            .leaveGroup
        ]
        
        collectionView.reloadData()
    }
}

// MARK: - Setup

private extension GroupDetailViewController {
    func setupViews() {
        view.backgroundColor = .white
        view.addSubview(containerView)
        
        containerView.addSubview(collectionView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            
            collectionView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            collectionView.topAnchor.constraint(equalTo: containerView.topAnchor),
            collectionView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor)
        ])
    }
    
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white

        appearance.titleTextAttributes = [
            .foregroundColor: ColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]

        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.title =  "Grup Bilgisi"
        navigationItem.leftBarButtonItem = backButton
    }
    
    private func createLayout() -> UICollectionViewLayout {
        let layout = UICollectionViewCompositionalLayout { [weak self] sectionIndex, _ in
            guard let self = self,
                  let section = GroupDetailSection(rawValue: sectionIndex) else {
                return nil
            }
            
            switch section {
            case .header:
                return self.createHeaderSection()
            case .members:
                return self.createMembersSection()
            case .footer:
                return self.createFooterSection()
            }
        }
        return layout
    }
    
    private func createHeaderSection() -> NSCollectionLayoutSection {
        let itemSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .estimated(200)
        )
        let item = NSCollectionLayoutItem(layoutSize: itemSize)
        
        let groupSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .estimated(200)
        )
        let group = NSCollectionLayoutGroup.horizontal(layoutSize: groupSize, subitems: [item])
        
        let section = NSCollectionLayoutSection(group: group)
        section.contentInsets = NSDirectionalEdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0)
        return section
    }
    
    private func createMembersSection() -> NSCollectionLayoutSection {
        let itemSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .absolute(48)
        )
        let item = NSCollectionLayoutItem(layoutSize: itemSize)
        
        let groupSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .absolute(48)
        )
        let group = NSCollectionLayoutGroup.horizontal(layoutSize: groupSize, subitems: [item])
        
        let section = NSCollectionLayoutSection(group: group)
        section.interGroupSpacing = 16
        section.contentInsets = NSDirectionalEdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0)
        
        let headerSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .absolute(44)
        )
        let header = NSCollectionLayoutBoundarySupplementaryItem(
            layoutSize: headerSize,
            elementKind: UICollectionView.elementKindSectionHeader,
            alignment: .top
        )
        section.boundarySupplementaryItems = [header]
        
        return section
    }
    
    private func createFooterSection() -> NSCollectionLayoutSection {
        let itemSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .absolute(44)
        )
        let item = NSCollectionLayoutItem(layoutSize: itemSize)
        
        let groupSize = NSCollectionLayoutSize(
            widthDimension: .fractionalWidth(1.0),
            heightDimension: .absolute(44)
        )
        let group = NSCollectionLayoutGroup.horizontal(layoutSize: groupSize, subitems: [item])
        
        let section = NSCollectionLayoutSection(group: group)
        section.interGroupSpacing = 0
        section.contentInsets = NSDirectionalEdgeInsets(top: 16, leading: 16, bottom: 24, trailing: 16)
        return section
    }
}

// MARK: - UICollectionViewDataSource
extension GroupDetailViewController: UICollectionViewDataSource, UICollectionViewDelegate {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return GroupDetailSection.allCases.count
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        guard let section = GroupDetailSection(rawValue: section) else { return 0 }
        
        switch section {
        case .header:
            return headerItem != nil ? 1 : 0
        case .members:
            return presenter.members?.count ?? 0
        case .footer:
            return footerItems.count
        }
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let section = GroupDetailSection(rawValue: indexPath.section) else {
            return collectionView.dequeueEmptyReusableCell(with: indexPath)
        }
        
        switch section {
        case .header:
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: GroupHeaderCell.reuseIdentifier, for: indexPath) as? GroupHeaderCell,
                  let headerItem = headerItem else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
            
            switch headerItem {
            case let .groupInfo(imageUrl, name, groupUserUser):
                cell.configure(imageUrl: imageUrl, name: name, groupUserUser: groupUserUser)
                cell.editButtonTapped = { [weak self] in
                    self?.presenter.didTapEditButton()
                }
            }
            return cell
            
        case .members:
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FollowerCollectionViewCell.reuseIdentifier, for: indexPath) as? FollowerCollectionViewCell,
                  let member = presenter.members?[indexPath.item] else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
            
            cell.configure(with: member.toCellModel(), delegate: presenter, at: indexPath)
            return cell
            
        case .footer:
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: GroupFooterCell.reuseIdentifier, for: indexPath) as? GroupFooterCell,
                  let item = footerItems[safe: indexPath.item]
            else {
                return collectionView.dequeueEmptyReusableCell(with: indexPath)
            }
           
            switch item {
            case .media(let count):
                cell.configure(title: "Medya ve belgeler", count: count, type: .media)
                cell.buttonTapped = { [weak self] in
                    self?.didTapMedia()
                }
            case .leaveGroup:
                cell.configure(title: "Gruptan ayrıl", type: .leave)
                cell.buttonTapped = { [weak self] in
                    self?.presenter.didTapLeaveGroup()
                }
            }
            
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let section = GroupDetailSection(rawValue: indexPath.section) else {
            return
        }
        switch section {
        case .members:
            presenter.didTapMemberProfile(at: indexPath)
        default: break
        }
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        viewForSupplementaryElementOfKind kind: String,
        at indexPath: IndexPath
    ) -> UICollectionReusableView {
        guard kind == UICollectionView.elementKindSectionHeader,
              let section = GroupDetailSection(rawValue: indexPath.section),
              section == .members,
              let headerView = collectionView.dequeueReusableSupplementaryView(
                ofKind: kind,
                withReuseIdentifier: GroupMembersHeaderView.reuseIdentifier,
                for: indexPath
              ) as? GroupMembersHeaderView else {
            return UICollectionReusableView()
        }
        
        headerView.addMemberTapped = { [weak self] in
            guard let self = self,
                  let groupId = self.presenter.groupDetailResponse?.groupId else { return }
            let viewController = CreateGroupChatBuilder.build(with: .addMembers(groupId: groupId))
            self.navigationController?.pushViewController(viewController, animated: true)
        }
        
        return headerView
    }
}
