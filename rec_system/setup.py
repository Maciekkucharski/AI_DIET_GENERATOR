from setuptools import setup, find_packages



VERSION = '0.0.1'
DESCRIPTION = 'package for recommending dishes in out taste profile, part of engeneering thesis'

# Setting up
setup(
    name="rec_system_trb",
    version=VERSION,
    author="Trb",
    author_email="<m.kucha@wp.pl>",
    description=DESCRIPTION,
    packages=find_packages(),
    install_requires=[],
    keywords=['python', ],
    classifiers=[
        "Development Status :: 1 - Planning",
        "Intended Audience :: Developers",
        "Programming Language :: Python :: 3",
        "Operating System :: Unix",
        "Operating System :: MacOS :: MacOS X",
        "Operating System :: Microsoft :: Windows",
    ]
)
