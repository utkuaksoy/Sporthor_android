//
//  ProfileViewController.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//
//

import BarVisibilityKit
import CommonKit
import DesignKit
import UIKit

final class ProfileViewController: BaseViewController, TabBarVisibility, NavigationBarVisibility {
      
    // MARK: - VIPER Variables
    var presenter: ProfilePresenterProtocol {
        get { return self.basePresenter as! ProfilePresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var containerStackView: UIStackView = {
        let stackView = UIStackView(
            arrangedSubviews: [
                headerView,
                collectionView
            ]
        )
        stackView.axis = .vertical
        stackView.distribution = .fill
        stackView.alignment = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var headerView: ProfileHeaderView = {
        let view = ProfileHeaderView()
        view.isHidden = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.sectionHeadersPinToVisibleBounds = true
        let view = UICollectionView(frame: .zero, collectionViewLayout: layout)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .white
        view.isPagingEnabled = false
        view.showsVerticalScrollIndicator = false
        view.contentInset = UIEdgeInsets(top: .zero, left: .zero, bottom: 16, right: .zero)
        view.contentInset.bottom = (tabBarController?.tabBar.frame.height ?? 0 + 16)
        return view
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
    
    private var threeDotButton: UIBarButtonItem {
        let threeDotImage = Asset.moreHoriz.image
        return UIBarButtonItem(
            image: threeDotImage,
            style: .plain,
            target: self,
            action: #selector(didTappedThreeDotButton)
        )
    }
    
    // MARK: - Private Properties
    
    private var dataSourceManager: ProfileCollectionViewDataSourceManager?
    private var delegate: ProfileCollectionViewDelegate?
    private var headerHeightConstraint: NSLayoutConstraint!
    
    private var isNavigationAndTabbarHidden: Bool {
        return navigationController?.viewControllers.count == 1
    }
        
    // MARK: - Lifecycles

    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
        setupViews()
        setupConstraints()
        setupUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
        configureTabBarVisibility(at: .willAppear(isHidden: false))
        configureNavigationBarVisibility(at: .willAppear(isHidden: isNavigationAndTabbarHidden))
        setUpNavigationBar()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: false))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if isMovingFromParent {
            configureTabBarVisibility(at: .willDisappear)
            configureNavigationBarVisibility(at: .willDisappear)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if isMovingFromParent {
            configureTabBarVisibility(at: .didDisappear)
        }
    }
        
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
    
    @objc
    private func didTappedThreeDotButton() {
        presenter.navigateProfileSetting()
    }
}

// MARK: - Setup

private extension ProfileViewController {
    func setupUI() {
        setUpNavigationBar()
        setupHeaderView()
        setupCollectionView()
    }
    
    func setUpNavigationBar() {
        guard !isNavigationAndTabbarHidden else {
            navigationController?.setNavigationBarHidden(isNavigationAndTabbarHidden, animated: false)
            return
        }
        configureNavigationBar()
    }
    
    func configureNavigationBar() {
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
        navigationItem.title = presenter.userName
        navigationItem.leftBarButtonItem = backButton
        navigationItem.rightBarButtonItem = presenter.checkUserIdReturnMyUser() ? nil : threeDotButton
    }
    
    func setupCollectionView() {
        dataSourceManager = ProfileCollectionViewDataSourceManager()
        dataSourceManager?.setDelegates(with: presenter)
        collectionView.dataSource = dataSourceManager
        delegate = ProfileCollectionViewDelegate(output: self)
        collectionView.delegate = delegate
        registerCell()
    }
    
    func setupHeaderView() {
        headerHeightConstraint = headerView.heightAnchor.constraint(equalToConstant: 40)
        headerHeightConstraint.isActive = true
    }
    
    func registerCell() {
        collectionView.register(
            ProfileInfoCollectionViewCell.self,
            forCellWithReuseIdentifier: ProfileInfoCollectionViewCell.reuseIdentifier
        )
        collectionView.register(
            TeamsContainerCell.self,
            forCellWithReuseIdentifier: TeamsContainerCell.reuseIdentifier
        )
        collectionView.register(
            ProfileAboutCell.self,
            forCellWithReuseIdentifier: ProfileAboutCell.reuseIdentifier
        )
        collectionView.register(
            ProfileActionButtonsCell.self,
            forCellWithReuseIdentifier: ProfileActionButtonsCell.reuseIdentifier
        )
        collectionView.register(
            NextMatchesCell.self,
            forCellWithReuseIdentifier: NextMatchesCell.reuseIdentifier
        )
        collectionView.register(
            EmptyViewCell.self,
            forCellWithReuseIdentifier: EmptyViewCell.reuseIdentifier
        )
        collectionView.register(
            PostImagesCell.self,
            forCellWithReuseIdentifier: PostImagesCell.reuseIdentifier
        )
        collectionView.register(
            MatchesCell.self,
            forCellWithReuseIdentifier: MatchesCell.reuseIdentifier
        )
        collectionView.register(
            TeamSquadCell.self,
            forCellWithReuseIdentifier: TeamSquadCell.reuseIdentifier
        )
        collectionView.register(
            PersonalInformationCell.self,
            forCellWithReuseIdentifier: PersonalInformationCell.reuseIdentifier
        )
        collectionView.register(
            CurrentTeamsContainerCell.self,
            forCellWithReuseIdentifier: CurrentTeamsContainerCell.reuseIdentifier
        )
        collectionView.register(
            FeaturedSkillsCell.self,
            forCellWithReuseIdentifier: FeaturedSkillsCell.reuseIdentifier
        )
        collectionView.register(
            TournamentsContainerCell.self,
            forCellWithReuseIdentifier: TournamentsContainerCell.reuseIdentifier
        )
        collectionView.register(
            CareerHistoryContainerCell.self,
            forCellWithReuseIdentifier: CareerHistoryContainerCell.reuseIdentifier
        )
        collectionView.registerEmptyCell()
        collectionView.registerEmptyHeaderReusableView()
        collectionView.register(
            ProfileSectionHeaderView.self,
            forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader,
            withReuseIdentifier: ProfileSectionHeaderView.reuseIdentifier
        )
    }
    
    func setupViews() {
        view.addSubview(containerView)
        containerView.addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            containerView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            containerView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            
            containerStackView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStackView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStackView.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor)
        ])
    }
}

extension ProfileViewController: ProfileCollectionViewDelegateProtocol {
    func shouldHideHeaderView(_ shouldHide: Bool) {
        let targetHeight: CGFloat = shouldHide ? 0 : 40
        let targetAlpha: CGFloat = shouldHide ? 0 : 1

        guard headerHeightConstraint.constant != targetHeight else { return }

        UIView.animate(withDuration: 0.3, delay: 0, options: [.curveEaseInOut], animations: {
            self.headerHeightConstraint.constant = targetHeight
            self.headerView.alpha = targetAlpha
            self.view.layoutIfNeeded()
        })
    }
}

extension ProfileViewController: ProfilePresenterDelegate {
    
    func refresh(with viewModel: ProfileViewModel) {
        dataSourceManager?.update(viewModel: viewModel)
        delegate?.update(viewModel: viewModel)
        collectionView.reloadData()
    }
    
    func configureHeaderView(info: ProfileInfoModel?) {
        guard let info = info else { return }
        if info.isCurrentUser, isNavigationAndTabbarHidden {
            headerView.configure(delegate: presenter, userName: info.username)
            headerView.isHidden = false
        } else {
            headerView.isHidden = true
        }
    }
    
    func reloadSection(with section: Int) {
        guard section < collectionView.numberOfSections else { return }
        UIView.performWithoutAnimation { [weak self] in
            self?.collectionView.reloadSections(IndexSet(integer: section))
        }
    }
    
    func scrollTo(indexPath: IndexPath) {
        collectionView.scrollToItem(at: indexPath, at: .top, animated: true)
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
}
