//
//  NextOnboardingViewController.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import UIKit
import ComponentKit

final class NextOnboardingViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: NextOnboardingPresenterProtocol {
        get { return self.basePresenter as! NextOnboardingPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var gradientView: CKGradientView = {
        let gradientView = CKGradientView(colors: [DesignKitColorName.successLighter100.color, .white])
        gradientView.translatesAutoresizingMaskIntoConstraints = false
        return gradientView
    }()
    
    private lazy var closeButton: CKButton = {
        let button = CKButton(delegate: self,
                              image: Asset.close.image,
                              tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.isPagingEnabled = true
        collectionView.isScrollEnabled = true
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.backgroundColor = .clear
        return collectionView
    }()
    
    private lazy var pageControl: CKWormPageControl = {
        let pageControl = CKWormPageControl(tintColor: .black.withAlphaComponent(0.2),
                                            currentPageTintColor: DesignKitColorName.contentStrong900.color,
                                            dotSize: 8,
                                            wormSize: 20,
                                            dotSpacing: 8)
        pageControl.translatesAutoresizingMaskIntoConstraints = false
        return pageControl
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: DesignKitL10n.NextOnboarding.continueButtonTitle,
                              titleColor: .white,
                              buttonBackgroundColor: DesignKitColorName.contentStrong900.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: true)
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        collectionView.collectionViewLayout.invalidateLayout()
    }
    
    // MARK: - Custom Methods
    private func changePage(isButtonClicked: Bool = false) {
        let indexPathsForVisibleItems = collectionView.indexPathsForVisibleItems.first
        guard let index = indexPathsForVisibleItems?.item else { return }
        presenter.changePage(to: index, isButtonClicked: isButtonClicked)
    }
}

// MARK: - NextOnboardingPresenterDelegate
extension NextOnboardingViewController: NextOnboardingPresenterDelegate {
    func setupView() {
        view.addSubview(gradientView)
        gradientView.addSubview(closeButton)
        gradientView.addSubview(collectionView)
        gradientView.addSubview(pageControl)
        gradientView.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            gradientView.topAnchor.constraint(equalTo: view.topAnchor),
            gradientView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            gradientView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            gradientView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            closeButton.topAnchor.constraint(equalTo: gradientView.safeAreaLayoutGuide.topAnchor, constant: 20),
            closeButton.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -24),
            closeButton.heightAnchor.constraint(equalToConstant: 32),
            closeButton.widthAnchor.constraint(equalToConstant: 32),
            
            continueButton.bottomAnchor.constraint(equalTo: gradientView.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -24),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
            
            pageControl.bottomAnchor.constraint(equalTo: continueButton.topAnchor, constant: -50),
            pageControl.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 48),
            pageControl.heightAnchor.constraint(equalToConstant: 8),
            
            collectionView.topAnchor.constraint(equalTo: closeButton.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: pageControl.topAnchor, constant: -34)
        ])
    }
    
    func didSetNumberOfPages(_ pages: Int) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            pageControl.setNumberOfPages(pages)
        }
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func scrollToItem(nextIndex: Int) {
        let contentOffsetX = CGFloat(nextIndex) * collectionView.frame.width
        collectionView.setContentOffset(CGPoint(x: contentOffsetX, y: 0), animated: true)
    }
    
    func changeButtonTitle(_ title: String) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.continueButton.setTitle(title)
        }
    }
    
    func didSetCurrentPageControl(_ page: Int) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.pageControl.setCurrentPage(page)
        }
    }
}

// MARK: - CKButtonDelegate
extension NextOnboardingViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 0:
            presenter.closeButtonTapped()
        case 1:
            changePage(isButtonClicked: true)
        default:
            break
        }
    }
}

// MARK: - UICollectionViewDataSource
extension NextOnboardingViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return presenter.onboarding.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = OnboardingCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        cell.bind(presenter.onboarding[indexPath.item])
        return cell
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension NextOnboardingViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return .zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return .zero
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        changePage()
    }
}
